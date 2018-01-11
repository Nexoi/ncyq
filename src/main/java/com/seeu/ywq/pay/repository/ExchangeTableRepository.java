package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.ExchangeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeTableRepository extends JpaRepository<ExchangeTable, Long> {
    List<ExchangeTable> findAllByType(@Param("type") ExchangeTable.TYPE type);

    ExchangeTable findByTypeAndFrom(@Param("type") ExchangeTable.TYPE type, @Param("from") BigDecimal from);

    ExchangeTable findByTypeAndTo(@Param("type") ExchangeTable.TYPE type, @Param("to") BigDecimal to);

}
