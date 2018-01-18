package com.seeu.ywq.gift.service;

import com.seeu.ywq.gift.dvo.RewardUserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RewardUserService {

    Page<RewardUserVO> findAll(Long uid, Pageable pageable);
}
