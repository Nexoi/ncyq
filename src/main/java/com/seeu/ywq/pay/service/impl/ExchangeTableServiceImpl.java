package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.pay.model.ExchangeTable;
import com.seeu.ywq.pay.repository.ExchangeTableRepository;
import com.seeu.ywq.pay.service.ExchangeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeTableServiceImpl implements ExchangeTableService {
    @Autowired
    private ExchangeTableRepository repository;

    @Override
    public List<ExchangeTable> findAllByType(ExchangeTable.TYPE type) {
        if (type == null) return new ArrayList<>();
        return repository.findAllByType(type);
    }

    @Override
    public ExchangeTable findByTypeAndFromPrice(ExchangeTable.TYPE type, BigDecimal price) {
        if (type == null || price == null) return null;
        return repository.findByTypeAndFrom(type, price);
    }
}
