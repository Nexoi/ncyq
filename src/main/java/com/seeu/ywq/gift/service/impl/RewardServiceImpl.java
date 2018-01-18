package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.gift.model.Reward;
import com.seeu.ywq.gift.repository.RewardRepository;
import com.seeu.ywq.gift.service.RewardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {
    @Resource
    private RewardRepository repository;

    @Override
    public Reward findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Reward> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Reward> findAll(Pageable pageable) {
        return repository.findAllByDiamondsNotNullOrderBySortId(pageable);
    }

    @Override
    public Reward save(Reward reward) {
        return repository.save(reward);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
