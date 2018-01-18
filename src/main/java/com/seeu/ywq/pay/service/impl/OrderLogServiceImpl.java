package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.repository.OrderLogRepository;
import com.seeu.ywq.pay.service.OrderLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderLogServiceImpl implements OrderLogService {
    @Resource
    private OrderLogRepository repository;

    @Override
    public OrderLog save(OrderLog orderLog) {
        if (orderLog == null) return null;
        return repository.save(orderLog);
    }
}
