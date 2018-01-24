package com.seeu.ywq.user.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.model.Address;

import java.util.List;

public interface AddressService {

    boolean exists(Long uid);

    Address findOne(Long id);

    Address findOneByUid(Long id, Long uid) throws ResourceNotFoundException;

    List<Address> findOneByUid(Long uid);

    Address save(Address address);

    Address update(Address address) throws ResourceNotFoundException;

    void delete(Long id);
}
