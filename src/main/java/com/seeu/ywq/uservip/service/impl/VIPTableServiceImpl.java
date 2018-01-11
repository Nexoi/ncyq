package com.seeu.ywq.uservip.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.model.VIPTable;
import com.seeu.ywq.uservip.repository.VIPTableRepository;
import com.seeu.ywq.uservip.service.VIPTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class VIPTableServiceImpl implements VIPTableService {
    @Resource
    private VIPTableRepository repository;

    @Override
    public List<VIPTable> findAll() {
        List<VIPTable> tables = repository.findAll();
        if (tables == null || tables.size() == 0) return new ArrayList<>();
        for (VIPTable table : tables) {
            table.setPrice(table.getPrice().setScale(2, BigDecimal.ROUND_UP));
        }
        return tables;
    }

    @Override
    public VIPTable findByDay(Long day) throws ResourceNotFoundException {
        VIPTable table = repository.findOne(day);
        if (table == null) throw new ResourceNotFoundException("Can not found VIP配置资源[day:" + day + "]");
        table.setPrice(table.getPrice().setScale(2, BigDecimal.ROUND_UP));
        return table;
    }

    @Override
    public BigDecimal getPriceByDay(Long day) throws ResourceNotFoundException {
        VIPTable table = repository.findOne(day);
        if (table == null) throw new ResourceNotFoundException("Can not found VIP配置资源[day:" + day + "]");
        return table.getPrice().setScale(2, BigDecimal.ROUND_UP);
    }

    @Override
    public VIPTable save(VIPTable table) {
        return repository.save(table);
    }
}
