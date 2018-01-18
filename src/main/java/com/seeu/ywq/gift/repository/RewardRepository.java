package com.seeu.ywq.gift.repository;

import com.seeu.ywq.gift.model.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    Page<Reward> findAllByDiamondsNotNullOrderBySortId(Pageable pageable);
}
