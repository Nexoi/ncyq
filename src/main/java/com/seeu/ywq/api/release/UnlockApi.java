package com.seeu.ywq.api.release;


import com.seeu.core.R;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "解锁资源", description = "需要付费的操作")
@RestController
@RequestMapping("/api/v1/unlock")
public class UnlockApi {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "解锁某一条动态", notes = "根据发布动态ID解锁动态【按天收取费用】")
    @PostMapping("/publish/{publishId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockPublish(@AuthenticationPrincipal UserLogin authUser,
                                        @PathVariable("publishId") Long publishId) {
        try {
            OrderLog log = orderService.createUnlockPublish(authUser.getUid(), publishId);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足").build());
        }
        return null;
    }

    @ApiOperation(value = "获取用户微信", notes = "短信发送给用户微信号码【按次收取费用】")
    @PostMapping("/wechat/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockWechat(@AuthenticationPrincipal UserLogin authUser,
                                       @PathVariable("uid") Long uid) {
        return null;
    }
}
