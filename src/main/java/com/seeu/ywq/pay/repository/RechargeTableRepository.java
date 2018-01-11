package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.RechargeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface RechargeTableRepository extends JpaRepository<RechargeTable, BigDecimal> {
}
