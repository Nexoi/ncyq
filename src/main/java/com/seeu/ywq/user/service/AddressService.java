package com.seeu.ywq.user.service;

import com.seeu.ywq.user.model.Address;

import java.util.List;

public interface AddressService {

    boolean exists(Long uid);

    Address findOne(Long id);

    List<Address> findOneByUid(Long uid);

    Address save(Address address);

    void delete(Long id);
}
