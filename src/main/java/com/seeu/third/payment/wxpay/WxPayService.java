package com.seeu.third.payment.wxpay;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.pay.model.WxPayTradeModel;
import com.seeu.ywq.pay.service.WxPayTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 2:10 PM
 * Describe:
 */

@Service
public class WxPayService {

    public String createOrder(String oid, BigDecimal price, String subject, String body) throws ActionParameterException {

        return null;
    }

    public String callBack(WxPayTradeModel model) {
        return "success";
    }

}
