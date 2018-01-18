package com.seeu.ywq.gift.repository;

import com.seeu.ywq.gift.model.RewardOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RewardOrderRepository extends JpaRepository<RewardOrder, String> {
    Page<RewardOrder> findAllByUid(@Param("uid") Long uid, Pageable pageable);

}
