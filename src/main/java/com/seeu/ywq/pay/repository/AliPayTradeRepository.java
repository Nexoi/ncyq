package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.AliPayTradeModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:45 PM
 * Describe:
 */

public interface AliPayTradeRepository extends JpaRepository<AliPayTradeModel, String> {
}
