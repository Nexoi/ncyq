package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayBalanceRepository extends JpaRepository<Balance, Long> {
}
