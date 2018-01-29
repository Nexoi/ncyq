package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.pay.model.AliPayTradeModel;
import com.seeu.ywq.pay.repository.AliPayTradeRepository;
import com.seeu.ywq.pay.service.AliPayTradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:54 PM
 * Describe:
 */
@Service
public class AliPayTraderServiceImpl implements AliPayTradeService {

    @Resource
    private AliPayTradeRepository repository;

    @Override
    public AliPayTradeModel findOne(String orderId) {
        return repository.findOne(orderId);
    }

    @Override
    public AliPayTradeModel save(AliPayTradeModel aliPayTradeModel) {
        return repository.save(aliPayTradeModel);
    }

    @Override
    public AliPayTradeModel update(AliPayTradeModel aliPayTradeModel) {
        return repository.save(aliPayTradeModel);
    }
}
