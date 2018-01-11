package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.service.UserVIPService;
import com.seeu.ywq.uservip.service.VIPTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "用户VIP", description = "个人VIP信息查看/成为VIP/查看VIP种类")
@RestController
@RequestMapping("/api/v1/user/vip")
public class VIPApi {

    @Autowired
    private VIPTableService vipTableService;
    @Autowired
    private UserVIPService userVIPService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "获取个人VIP信息")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser) {
        UserVIP vip = userVIPService.findOne(authUser.getUid());
        if (vip == null)
            return ResponseEntity.status(404).body(R.code(404).message("找不到您的会员卡信息，请确认是否购买"));
        if (vip.getTerminationDate() == null || vip.getTerminationDate().before(new Date()))
            vip.setVipLevel(UserVIP.VIP.none);
        vip.setUid(null);
        return ResponseEntity.ok(vip);
    }

    @ApiOperation(value = "查看VIP种类", notes = "比如：分年卡、月卡、季卡，每种卡对应其价格")
    @GetMapping("/list")
    public ResponseEntity list() {
        return ResponseEntity.ok(vipTableService.findAll());
    }

    @ApiOperation(value = "根据天数获取VIP卡信息", notes = "")
    @GetMapping("/day/{day}")
    public ResponseEntity get(@PathVariable Long day) {
        try {
            return ResponseEntity.ok(vipTableService.findByDay(day));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("无此会员卡可购买"));
        }
    }

    // 购买 VIP 卡
    // TODO
    @ApiOperation(value = "购买VIP卡（钻石）")
    @PostMapping("/buy-usediamond")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity buyUseDiamond(@AuthenticationPrincipal UserLogin authUser,
                                        Long day) {
        try {
            // 传回支付订单信息，完成支付
            // TODO...
            // 此处模拟成功
            return ResponseEntity.ok(orderService.createVIPCardUseDiamond(authUser.getUid(), day));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("无此VIP卡可以购买"));
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足，请充值后购买"));
        }
    }

    @ApiOperation(value = "购买VIP卡（支付宝）")
    @PostMapping("/buy-alipay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity buyAliPay(@AuthenticationPrincipal UserLogin authUser,
                                    Long day) {
        return null;
    }

    @ApiOperation(value = "购买VIP卡（微信）")
    @PostMapping("/buy-wechat")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity buyWeChat(@AuthenticationPrincipal UserLogin authUser,
                                    Long day) {
        return null;
    }
}
