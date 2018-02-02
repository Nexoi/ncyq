package com.seeu.third.payment.wxpay;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.pay.model.WxPayTradeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 2:10 PM
 * Describe:
 * <p>
 * update: 2018-02-03 使用 Apache Http 进行请求
 */

@Service
public class WxPayService {

    @Value("${wxpay.appid}")
    private String appid;

    @Value("${wxpay.mchid}")
    private String mch_id;

    @Value("${wxpay.notify_url}")
    private String notifyUrl;

    @Autowired
    private WxUtils wxUtils;

    public String createOrder(String oid, BigDecimal price, String body, String ipAddress, String deviceInfo) throws ActionParameterException, IOException {
        String placeUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("device_info", "WEB");
        parameters.put("body", body);
        parameters.put("nonce_str", wxUtils.gen32RandomString());
        parameters.put("notify_url", notifyUrl);
        parameters.put("out_trade_no", oid);
//        parameters.put("total_fee", price.multiply(BigDecimal.valueOf(100)).intValue());
        parameters.put("total_fee", 1);
        parameters.put("spbill_create_ip", ipAddress);
        parameters.put("trade_type", "APP");
        parameters.put("sign", wxUtils.createSign(parameters)); // 必须在最后
//        String result = restTemplate.postForObject(placeUrl, transferToXml(parameters), String.class);
        return wxUtils.executePost(placeUrl, parameters);
    }

    public String callBack(WxPayTradeModel model) {
        return "success";
    }


}
