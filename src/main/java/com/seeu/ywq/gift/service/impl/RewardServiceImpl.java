package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
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
    public Reward findOne(Long id) throws ResourceNotFoundException {
        if (id == null)
            throw new ResourceNotFoundException("Can not found Resource [" + id + "]");
        Reward reward = repository.findOne(id);
        if (reward == null)
            throw new ResourceNotFoundException("Can not found Resource [" + id + "]");
        return reward;
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
    public Reward update(Reward reward) throws ResourceNotFoundException {
        if (reward.getId() == null) throw new ResourceNotFoundException("Can not found Resource [ reward ]");
        Reward reward1 = repository.findOne(reward.getId());
        if (reward1 == null) throw new ResourceNotFoundException("Can not found Resource [" + reward.getId() + "]");
        return repository.save(reward);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
