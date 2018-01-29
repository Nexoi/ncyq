package com.seeu.ywq.api.release.pay;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.service.TradeService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 2:42 PM
 * Describe:
 */

@Api(tags = "第三方支付回调接口", description = "不需要操作")
@RestController
public class PayCallBackController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/api/v1/pay/order/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity checkOrder(@AuthenticationPrincipal UserLogin authUser,
                                     @PathVariable String orderId) {
        try {
            TradeModel trade = tradeService.findOne(orderId);
            if (trade.getUid().equals(authUser.getUid())) {
                trade.setDiamonds(null);
                return ResponseEntity.ok(trade);
            }
            // 不是自己的
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        }
    }
}
