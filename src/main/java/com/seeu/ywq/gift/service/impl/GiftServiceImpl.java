package com.seeu.ywq.gift.service.impl;

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
    public Gift findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Page<Gift> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
