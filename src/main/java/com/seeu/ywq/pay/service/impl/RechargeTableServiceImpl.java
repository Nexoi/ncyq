package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.pay.model.RechargeTable;
import com.seeu.ywq.pay.repository.RechargeTableRepository;
import com.seeu.ywq.pay.service.RechargeTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RechargeTableServiceImpl implements RechargeTableService {
    @Resource
    private RechargeTableRepository repository;

    @Override
    public RechargeTable save(RechargeTable rechargeTable) {
        return repository.save(rechargeTable);
    }

    @Override
    public RechargeTable findOne(BigDecimal price) {
        return repository.findOne(price);
    }

    @Override
    public List<RechargeTable> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(BigDecimal price) {
        if (price == null) return;
        if (null != repository.findOne(price))
            repository.delete(price);
    }
}
