package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.model.ExchangeTable;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeTableService {
    List<ExchangeTable> findAllByType(ExchangeTable.TYPE type);

    ExchangeTable findByTypeAndFromPrice(ExchangeTable.TYPE type, BigDecimal price);
}
