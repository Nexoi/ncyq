package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.WxPayTradeModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 3:45 PM
 * Describe:
 */

public interface WxPayTradeRepository extends JpaRepository<WxPayTradeModel, String> {
}
