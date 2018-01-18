package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.model.Address;
import com.seeu.ywq.user.repository.AddressRepository;
import com.seeu.ywq.user.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressRepository repository;

    @Override
    public boolean exists(Long uid) {
        return repository.existsByUid(uid);
    }

    @Override
    public Address findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Address> findOneByUid(Long uid) {
        return repository.findAllByUid(uid);
    }

    @Override
    public Address save(Address address) {
        return repository.save(address);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
