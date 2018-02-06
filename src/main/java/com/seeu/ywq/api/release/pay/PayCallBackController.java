package com.seeu.ywq.api.release.pay;

import com.seeu.third.payment.alipay.AliPayService;
import com.seeu.third.payment.wxpay.WxPayService;
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

    @RequestMapping(value = "/wxpay/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String wx(HttpServletRequest request, HttpServletResponse response) {
        try {
            return wxPayService.callBack(request, response);
        } catch (Exception e) {
            response.setHeader("Content-type", "application/xml");
            return "<xml>\n" +
                    "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                    "  <return_msg><![CDATA[]]></return_msg>\n" +
                    "</xml>";
        }
    }

    @RequestMapping(value = "/alipay/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String ali(HttpServletRequest request) {
        try {
//            Enumeration<String> parameters = request.getParameterNames();
//            Map map = new HashMap();
//            while (parameters.hasMoreElements()) {
//                String key = parameters.nextElement().toString();
//                String value = request.getParameter(key);
//                map.put(key, value);
//            }
            return aliPayService.callBack(request);
        } catch (Exception e) {
//            testXService.info(e.getMessage());
//            testXService.info(e.getCause().getMessage());
//            for (StackTraceElement element : e.getStackTrace()) {
//                testXService.info(element.getClassName() + " # " + element.getMethodName());
//            }
            return "failure";
        }
    }

}
