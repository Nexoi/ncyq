package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.model.AliPayTradeModel;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:46 PM
 * Describe:
 */

public interface AliPayTradeService {
    AliPayTradeModel findOne(String orderId);

    AliPayTradeModel save(AliPayTradeModel aliPayTradeModel);

    AliPayTradeModel update(AliPayTradeModel aliPayTradeModel); // 和 save 一个意思
}
