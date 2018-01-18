package com.seeu.ywq.gift.service;

import com.seeu.ywq.gift.model.GiftOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftOrderService {
    GiftOrder save(GiftOrder giftOrder);

    GiftOrder findOne(String orderId);

    Page<GiftOrder> findAllByUid(Long uid, Pageable pageable);
}
