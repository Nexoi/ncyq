package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.*;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.pay.service.RechargeTableService;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.utils.DateFormatterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户账户", description = "充值/提现/金币兑换/交易记录/账户信息")
@RestController
@RequestMapping("/api/v1/user")
public class UserBalanceApi {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RechargeTableService rechargeTableService;

    @ApiOperation(value = "查看交易记录", notes = "查看自己的余额系统收支情况")
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listTransactions(@AuthenticationPrincipal UserLogin authUser,
                                           @RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.queryAll(authUser.getUid(), new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime"))));
    }

    @ApiOperation(value = "查看账户各项额度记录", notes = "查看自己账户余额、相册收入、打赏收入、提现记录等")
    @GetMapping("/account")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyBalanceDetail(@AuthenticationPrincipal UserLogin authUser) {
        Balance balance = null;
        try {
            balance = balanceService.queryDetail(authUser.getUid());
        } catch (NoSuchUserException e) {
            // 初始化账户
            balanceService.initAccount(authUser.getUid(), null);
            balance = new Balance();
            balance.setBindUid(null);
        }
        return ResponseEntity.ok(balance);
    }

    @ApiOperation(value = "查看余额", notes = "查看自己账户余额")
    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyBalance(@AuthenticationPrincipal UserLogin authUser) {
        Long diamonds = 0L;
        try {
            diamonds = balanceService.query(authUser.getUid());
        } catch (NoSuchUserException e) {
            // 初始化账户
            balanceService.initAccount(authUser.getUid(), null);
        }
        Map map = new HashMap();
        map.put("balance", diamonds);
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "获取可以兑换的金币数", notes = "传入钻石额度，获取可以得到的金币数量")
    @GetMapping("/balance/exchange")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getExchange(@AuthenticationPrincipal UserLogin authUser,
                                      Long diamonds) {
        try {
            return ResponseEntity.ok(orderService.queryExchange(authUser.getUid(), ExchangeTable.TYPE.DIAMOND2COIN, diamonds));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("传入参数必须为整数"));
        }
    }


    @ApiOperation(value = "兑换", notes = "将钻石兑换为金币")
    @PostMapping("/balance/exchange")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity exchange(@AuthenticationPrincipal UserLogin authUser,
                                   Long diamonds) {
        try {
            return ResponseEntity.ok(orderService.createTransferDiamondsToCoins(authUser.getUid(), diamonds));
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("余额不足"));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("操作不被允许，兑换的额度不能为 0 "));
        }
    }

    @ApiOperation(value = "获取可以充值的钻石数", notes = "传入RMB额度，获取可以得到的钻石数量")
    @GetMapping("/balance/recharge")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getRecharge(@AuthenticationPrincipal UserLogin authUser,
                                      BigDecimal price) {
        try {
            return ResponseEntity.ok(orderService.queryExchange(authUser.getUid(), ExchangeTable.TYPE.RMB2DIAMOND, price));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("传入参数必须为整数"));
        }
    }

    @ApiOperation(value = "获取充值需要花费的RMB", notes = "传入钻石数，获取需要花费的RMB")
    @GetMapping("/balance/recharge/reverse")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getRechargeReverse(@AuthenticationPrincipal UserLogin authUser,
                                             Long diamonds) {
        try {
            return ResponseEntity.ok(orderService.queryExchangeReverse(authUser.getUid(), diamonds));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("传入参数必须为正整数"));
        }
    }

    @ApiOperation(value = "获取充值列表")
    @GetMapping("/balance/recharge/list")
    public ResponseEntity listRechargeTable() {
        List list = rechargeTableService.findAll();
        if (list == null || list.size() == 0)
            return ResponseEntity.status(204).body(R.code(204).message("资源为空，找不到充值列表信息"));
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "充值", notes = "给自己充值一定额度的钻石，服务器创建订单，客户端将订单信息发送到支付宝/微信进行支付，完成后服务器会自动校验支付情况。重新刷新余额即可查看结果")
    @PostMapping("/balance/recharge")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity recharge(@AuthenticationPrincipal UserLogin authUser,
                                   @RequestParam(required = true) Double price) {
        try {
            if (null == rechargeTableService.findOne(BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_UP)))
                return ResponseEntity.badRequest().body(R.code(400).message("无法充值该额度"));
            // 当然不能如下这样操作：
            balanceService.update(dateFormatterService.getyyyyMMddHHmmssS().format(new Date()), authUser.getUid(), OrderLog.EVENT.RECHARGE, 1000L);
            return ResponseEntity.ok(R.code(200).message("充值成功！"));
        } catch (BalanceNotEnoughException e) {
            return null; // 不可能发生的事情
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("充值额度不能为负数！"));
        }
    }

    @ApiOperation(value = "提现", notes = "提现操作会被视作申请提现操作。管理员后台同意之后会打款至对应的账号")
    @PostMapping("/balance/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity withdraw(@AuthenticationPrincipal UserLogin authUser,
                                   OrderRecharge.PAY_METHOD payMethod,
                                   @ApiParam(value = "用户支付宝/微信账户ID")
                                           String payId,
                                   @ApiParam(value = "用户支付宝/微信名字")
                                           String accountName,
                                   Long diamonds) {
        try {
            balanceService.update(dateFormatterService.getyyyyMMddHHmmssS().format(new Date()), authUser.getUid(), OrderLog.EVENT.WITHDRAW, diamonds);
            return ResponseEntity.ok(R.code(200).message("提现成功！"));
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("余额不足！"));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("提现额度不能为负数！"));
        }
    }

    @Autowired
    DateFormatterService dateFormatterService;

    @ApiOperation(value = "获取可以提现的RMB数", notes = "传入钻石数，获取提现可得的RMB数")
    @GetMapping("/balance/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getWithdraw(@AuthenticationPrincipal UserLogin authUser,
                                      Long diamonds) {
        try {
            return ResponseEntity.ok(orderService.queryExchangeReverse(authUser.getUid(), diamonds));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("传入参数必须为正整数"));
        }
    }
}
