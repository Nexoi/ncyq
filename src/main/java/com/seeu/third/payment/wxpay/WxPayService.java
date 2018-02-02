package com.seeu.third.payment.wxpay;

import com.alibaba.fastjson.JSON;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.pay.model.WxPayTradeModel;
import com.seeu.ywq.pay.service.WxPayTradeService;
import org.apache.commons.collections.map.FixedSizeSortedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.beans.XMLEncoder;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 2:10 PM
 * Describe:
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
    @Autowired
    private RestTemplate restTemplate;

    public String createOrder(String oid, BigDecimal price, String body, String ipAddress, String deviceInfo) throws ActionParameterException {
        String placeUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("device_info", deviceInfo);
        parameters.put("body", body);
        parameters.put("nonce_str", wxUtils.gen32RandomString());
        parameters.put("notify_url", notifyUrl);
        parameters.put("out_trade_no", oid);
//        parameters.put("total_fee", price.multiply(BigDecimal.valueOf(100)).intValue());
        parameters.put("total_fee", 1);
        parameters.put("spbill_create_ip", ipAddress);
        parameters.put("trade_type", "APP");
        parameters.put("sign", wxUtils.createSign(parameters)); // 必须在最后
        String result = restTemplate.postForObject(placeUrl, transferToXml(parameters), String.class);
        return result;
    }

    public String callBack(WxPayTradeModel model) {
        return "success";
    }

    private String transferToXml(SortedMap<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            sb.append("<").append(key).append(">")
                    .append(map.get(key))
                    .append("</").append(key).append(">");
        }
        return sb.append("</xml>").toString();
    }
}
