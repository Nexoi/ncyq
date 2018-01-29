package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.model.WxPayTradeModel;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:46 PM
 * Describe:
 */

public interface WxPayTradeService {
    WxPayTradeModel findOne(String orderId);

    WxPayTradeModel save(WxPayTradeModel model);

    WxPayTradeModel update(WxPayTradeModel model);
}
