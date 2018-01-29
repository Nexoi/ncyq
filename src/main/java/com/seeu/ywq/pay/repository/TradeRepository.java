package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.TradeModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 11:30 AM
 * Describe:
 */

public interface TradeRepository extends JpaRepository<TradeModel, String> {
}
