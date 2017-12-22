package com.seeu.ywq.release.repository;

import com.seeu.ywq.pay.model.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, String> {
}
