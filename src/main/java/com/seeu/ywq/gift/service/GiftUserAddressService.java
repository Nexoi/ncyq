package com.seeu.ywq.gift.service;

import com.seeu.ywq.gift.model.GiftUserAddress;

public interface GiftUserAddressService {
    GiftUserAddress save(GiftUserAddress address);

    GiftUserAddress findOne(Long orderId);
}
