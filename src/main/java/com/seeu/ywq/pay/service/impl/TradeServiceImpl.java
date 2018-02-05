package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.repository.TradeRepository;
import com.seeu.ywq.pay.service.TradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 11:32 AM
 * Describe:
 */
@Service
public class TradeServiceImpl implements TradeService {

    @Resource
    private TradeRepository repository;

    @Override
    public TradeModel findOne(String orderId) throws ResourceNotFoundException {
        TradeModel trade = repository.findOne(orderId);
        if (trade == null) throw new ResourceNotFoundException("找不到该账单");
        return trade;
    }

    @Override
    public TradeModel save(TradeModel trade) throws ActionParameterException {
        if (trade == null || trade.getOrderId() == null)
            throw new ActionParameterException("trade");
        return repository.save(trade);
    }

    /**
     * 该订单是否处理完毕
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean hasProcessed(String orderId) {
        TradeModel model = repository.findOne(orderId);
        if (model == null) return false;
        if (model.getStatus() == TradeModel.TRADE_STATUS.WAIT_BUYER_PAY) return false;
        if (model.getStatus() == TradeModel.TRADE_STATUS.TRADE_SUCCESS) return true;
        if (model.getStatus() == TradeModel.TRADE_STATUS.TRADE_FINISHED) return true;
        if (model.getStatus() == TradeModel.TRADE_STATUS.TRADE_CLOSED) return true;
        return false;
    }

    @Override
    public TradeModel updateStatus(String orderId, TradeModel.TRADE_STATUS status) throws ActionParameterException, ResourceNotFoundException {
        TradeModel model = findOne(orderId);
        model.setStatus(status);
        return repository.save(model);
    }
}
