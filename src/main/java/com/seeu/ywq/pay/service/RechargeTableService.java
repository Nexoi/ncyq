package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.model.RechargeTable;

import java.math.BigDecimal;
import java.util.List;

public interface RechargeTableService {
    RechargeTable save(RechargeTable rechargeTable);

    RechargeTable findOne(BigDecimal price);

    List<RechargeTable> findAll();

    void delete(BigDecimal price);
}
