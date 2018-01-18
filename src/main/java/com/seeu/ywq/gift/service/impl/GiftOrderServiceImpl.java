package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.repository.GiftOrderRepository;
import com.seeu.ywq.gift.service.GiftOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GiftOrderServiceImpl implements GiftOrderService {
    @Resource
    private GiftOrderRepository repository;

    @Override
    public GiftOrder save(GiftOrder giftOrder) {
        return repository.save(giftOrder);
    }

    @Override
    public GiftOrder findOne(String orderId) {
        return repository.findOne(orderId);
    }

    @Override
    public Page<GiftOrder> findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUid(uid, pageable);
    }
}
