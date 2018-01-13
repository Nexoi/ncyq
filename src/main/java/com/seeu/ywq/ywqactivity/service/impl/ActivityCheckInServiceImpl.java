package com.seeu.ywq.ywqactivity.service.impl;

import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import com.seeu.ywq.ywqactivity.repository.ActivityCheckInRepository;
import com.seeu.ywq.ywqactivity.service.ActivityCheckInService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ActivityCheckInServiceImpl implements ActivityCheckInService {
    @Resource
    private ActivityCheckInRepository repository;

    @Override
    public ActivityCheckIn findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Page<ActivityCheckIn> findAllByActivityId(Long activityId, Pageable pageable) {
        return repository.findAllByActivityId(activityId, pageable);
    }

    @Override
    public Page<ActivityCheckIn> findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUid(uid, pageable);
    }

    @Override
    public Page<ActivityCheckIn> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ActivityCheckIn save(ActivityCheckIn activityCheckIn) {
        return repository.save(activityCheckIn);
    }
}
