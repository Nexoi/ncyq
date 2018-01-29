package com.seeu.ywq.api.release.pay;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.service.AliPayTradeService;
import com.seeu.ywq.pay.service.TradeService;
import com.seeu.ywq.pay.service.WxPayTradeService;
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

@Api(tags = "第三方支付查询结果", description = "查询订单状态详情")
@RestController
public class PayTradeInfoController {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private WxPayTradeService wxPayTradeService;
    @Autowired
    private AliPayTradeService aliPayTradeService;

    @GetMapping("/api/v1/pay/order/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity checkOrder(@AuthenticationPrincipal UserLogin authUser,
                                     @PathVariable String orderId) {
        try {
            TradeModel trade = tradeService.findOne(orderId);
            if (trade.getUid().equals(authUser.getUid())) {
                trade.setExtraData(null);
                return ResponseEntity.ok(trade);
            }
            // 不是自己的
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        }
    }

    @GetMapping("/api/v1/pay/order/{orderId}/detail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity checkOrderDeatil(@AuthenticationPrincipal UserLogin authUser,
                                           @PathVariable String orderId) {
        try {
            TradeModel trade = tradeService.findOne(orderId);
            if (trade.getUid().equals(authUser.getUid())) {
                trade.setExtraData(null);
                String oid = trade.getOrderId();
                if (trade.getPayment() == null)
                    return ResponseEntity.ok(trade);
                if (trade.getPayment() == TradeModel.PAYMENT.ALIPAY)
                    return ResponseEntity.ok(aliPayTradeService.findOne(oid));
                if (trade.getPayment() == TradeModel.PAYMENT.WECHAT)
                    return ResponseEntity.ok(wxPayTradeService.findOne(oid));
            }
            // 不是自己的
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("无此订单"));
        }
    }
}
