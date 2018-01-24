package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.gift.model.Gift;
import com.seeu.ywq.gift.repository.GiftRepository;
import com.seeu.ywq.gift.service.GiftService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GiftServiceImpl implements GiftService {

    @Resource
    private GiftRepository repository;

    @Override
    public Gift save(Gift gift) {
        return repository.save(gift);
    }

    @Override
    public Gift findOne(Long id) throws ResourceNotFoundException {
        if (id == null) throw new ResourceNotFoundException("Can not found Resource [Gift: " + id + "]");
        Gift gift = repository.findOne(id);
        if (gift == null) throw new ResourceNotFoundException("Can not found Resource [Gift: " + id + "]");
        return gift;
    }

    @Override
    public Page<Gift> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Gift update(Gift gift) throws ResourceNotFoundException {
        if (gift.getId() == null) throw new ResourceNotFoundException("Can not found Resource [ reward ]");
        Gift gift1 = repository.findOne(gift.getId());
        if (gift1 == null) throw new ResourceNotFoundException("Can not found Resource [" + gift.getId() + "]");
        return repository.save(gift);    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
