package com.seeu.ywq.uservip.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.model.VIPTable;
import com.seeu.ywq.uservip.repository.VIPTableRepository;
import com.seeu.ywq.uservip.service.VIPTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class VIPTableServiceImpl implements VIPTableService {
    @Resource
    private VIPTableRepository repository;

    @Override
    public List<VIPTable> findAll() {
        return repository.findAll();
    }

    @Override
    public VIPTable findByDay(Long day) throws ResourceNotFoundException {
        VIPTable table = repository.findOne(day);
        if (table == null) throw new ResourceNotFoundException("Can not found VIP配置资源[day:" + day + "]");
        return table;
    }

    @Override
    public BigDecimal getPriceByDay(Long day) throws ResourceNotFoundException {
        VIPTable table = repository.findOne(day);
        if (table == null) throw new ResourceNotFoundException("Can not found VIP配置资源[day:" + day + "]");
        return table.getPrice().setScale(2, BigDecimal.ROUND_UP);
    }
}
