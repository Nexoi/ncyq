package com.seeu.ywq.api.release;

import com.seeu.core.R;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户账户", description = "充值/提现")
@RestController
@RequestMapping("/api/v1/user/balance")
public class UserBalanceApi {
    @Autowired
    private BalanceService balanceService;

    @ApiOperation(value = "查看余额", notes = "查看自己账户余额")
    @GetMapping
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

    @ApiOperation(value = "充值", notes = "给自己充值一定额度的钻石，服务器创建订单，客户端将订单信息发送到支付宝/微信进行支付，完成后服务器会自动校验支付情况。重新刷新余额即可查看结果")
    @PostMapping("/recharge")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity recharge(@AuthenticationPrincipal UserLogin authUser,
                                   Long diamonds) {
        balanceService.plus(authUser.getUid(), diamonds, OrderLog.EVENT.RECHARGE);
        return ResponseEntity.ok(R.code(200).message("充值成功！"));
    }

    @ApiOperation(value = "提现", notes = "提现操作会被视作申请提现操作。管理员后台同意之后会打款至对应的账号")
    @PostMapping(value = "/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity withdraw(@AuthenticationPrincipal UserLogin authUser,
                                   OrderRecharge.PAY_METHOD payMethod,
                                   @ApiParam(value = "用户支付宝/微信账户ID")
                                           String payId,
                                   @ApiParam(value = "用户支付宝/微信名字")
                                           String accountName,
                                   Long diamonds) {
        try {
            balanceService.minus(authUser.getUid(), diamonds);
            return ResponseEntity.ok(R.code(200).message("提现成功！"));
        } catch (BalanceNotEnoughException e) {
            e.printStackTrace();
            return ResponseEntity.ok(R.code(200).message("余额不足！"));
        }
    }
}
