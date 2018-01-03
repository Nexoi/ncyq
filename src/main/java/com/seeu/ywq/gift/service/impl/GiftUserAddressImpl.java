package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.gift.model.GiftUserAddress;
import com.seeu.ywq.gift.repository.GiftUserAddressRepository;
import com.seeu.ywq.gift.service.GiftUserAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GiftUserAddressImpl implements GiftUserAddressService {
    @Resource
    private GiftUserAddressRepository repository;

    @Override
    public GiftUserAddress save(GiftUserAddress address) {
        return repository.save(address);
    }

    @Override
    public GiftUserAddress findOne(Long orderId) {
        return repository.findOne(orderId);
    }
}
