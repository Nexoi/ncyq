package com.seeu.ywq.gift.service;

import com.seeu.ywq.gift.model.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RewardService {
    Reward findOne(Long id);

    List<Reward> findAll();

    Page<Reward> findAll(Pageable pageable);

    Reward save(Reward reward);

    void delete(Long id);
}
