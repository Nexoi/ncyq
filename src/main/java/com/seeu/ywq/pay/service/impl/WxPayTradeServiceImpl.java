package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.pay.model.WxPayTradeModel;
import com.seeu.ywq.pay.repository.WxPayTradeRepository;
import com.seeu.ywq.pay.service.WxPayTradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:56 PM
 * Describe:
 */
@Service
public class WxPayTradeServiceImpl implements WxPayTradeService {
    @Resource
    private WxPayTradeRepository repository;

    @Override
    public WxPayTradeModel findOne(String orderId) {
        return repository.findOne(orderId);
    }

    @Override
    public WxPayTradeModel save(WxPayTradeModel model) {
        return repository.save(model);
    }

    @Override
    public WxPayTradeModel update(WxPayTradeModel model) {
        return repository.save(model);
    }
}
