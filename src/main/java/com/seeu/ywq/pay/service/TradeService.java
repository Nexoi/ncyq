package com.seeu.ywq.pay.service;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.model.TradeModel;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 11:30 AM
 * Describe:
 */

public interface TradeService {

    TradeModel findOne(String orderId) throws ResourceNotFoundException;

    TradeModel save(TradeModel trade) throws ActionParameterException;

    boolean hasProcessed(String orderId);

    TradeModel updateStatus(String orderId, TradeModel.TRADE_STATUS status) throws ActionParameterException, ResourceNotFoundException;
}
