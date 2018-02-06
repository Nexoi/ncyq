package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PayBalanceRepository extends JpaRepository<Balance, Long> {
    Integer countAllByBindUid(@Param("bindUid") Long bindUid);
}
