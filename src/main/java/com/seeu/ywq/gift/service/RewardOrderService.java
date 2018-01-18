package com.seeu.ywq.gift.service;

import com.seeu.ywq.gift.model.RewardOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RewardOrderService {
    RewardOrder save(RewardOrder rewardOrder);

    RewardOrder findOne(String orderId);

    Page<RewardOrder> findAllByUid(Long uid, Pageable pageable);
}
