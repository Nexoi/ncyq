package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.model.Address;
import com.seeu.ywq.user.repository.AddressRepository;
import com.seeu.ywq.user.service.AddressService;
import org.springframework.beans.BeanUtils;
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
    public Address findOneByUid(Long id, Long uid) throws ResourceNotFoundException {
        if (id == null || uid == null) throw new ResourceNotFoundException("无此地址");
        Address address = repository.findOneByIdAndUid(id, uid);
        if (address == null) throw new ResourceNotFoundException("无此地址");
        return address;
    }

    @Override
    public List<Address> findOneByUid(Long uid) {
        return repository.findAllByUid(uid);
    }

    @Override
    public Address save(Address address) {
        address.setId(null);
        return repository.save(address);
    }

    @Override
    public Address update(Address address) throws ResourceNotFoundException {
        Address address1 = findOneByUid(address.getId(), address.getUid());
        BeanUtils.copyProperties(address, address1);
        return save(address1);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
