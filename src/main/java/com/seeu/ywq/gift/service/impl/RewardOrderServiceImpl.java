package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.gift.model.RewardOrder;
import com.seeu.ywq.gift.repository.RewardOrderRepository;
import com.seeu.ywq.gift.service.RewardOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RewardOrderServiceImpl implements RewardOrderService {

    @Resource
    private RewardOrderRepository repository;

    @Override
    public RewardOrder save(RewardOrder rewardOrder) {
        return repository.save(rewardOrder);
    }

    @Override
    public RewardOrder findOne(String orderId) {
        return repository.findOne(orderId);
    }

    @Override
    public Page<RewardOrder> findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUid(uid, pageable);
    }
}
