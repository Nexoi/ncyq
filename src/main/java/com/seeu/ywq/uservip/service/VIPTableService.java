package com.seeu.ywq.uservip.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.model.VIPTable;

import java.math.BigDecimal;
import java.util.List;

public interface VIPTableService {
    List<VIPTable> findAll();

    VIPTable findByDay(Long day) throws ResourceNotFoundException;

    BigDecimal getPriceByDay(Long day) throws ResourceNotFoundException;


    VIPTable save(VIPTable table);
}
