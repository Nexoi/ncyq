package com.seeu.ywq.api.release.pay;

import com.alipay.api.AlipayApiException;
import com.seeu.third.payment.alipay.AliPayService;
import com.seeu.third.payment.wxpay.WxPayService;
import com.seeu.ywq.pay.model.AliPayTradeModel;
import com.seeu.ywq.pay.model.WxPayTradeModel;
import com.seeu.ywq.test.TestX;
import com.seeu.ywq.test.TestXService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:37 PM
 * Describe:
 * <p>
 * 回调接口
 */
@Api(tags = "第三方支付回调接口", description = "支付宝／微信调用")
@RestController
@RequestMapping("/api/payment")
public class PayCallBackController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private TestXService testXService;

    @RequestMapping(value = "/wxpay/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String wx(HttpServletRequest request, HttpServletResponse response) {
        try {
            return wxPayService.callBack(request, response);
        } catch (Exception e) {
            testXService.info("微信异常" + e.getMessage());
            response.setHeader("Content-type", "application/xml");
            return "<xml>\n" +
                    "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                    "  <return_msg><![CDATA[]]></return_msg>\n" +
                    "</xml>";
        }
    }

    @RequestMapping(value = "/alipay/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String ali(AliPayTradeModel aliPayTradeModel) {
        try {
            return aliPayService.callBack(aliPayTradeModel);
        } catch (AlipayApiException e) {
            return "failure";
        }
    }

}
