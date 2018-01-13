package com.seeu.ywq.ywqactivity.service.impl;

import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import com.seeu.ywq.ywqactivity.repository.ActivityCheckInRepository;
import com.seeu.ywq.ywqactivity.service.ActivityCheckInService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
    public ActivityCheckIn findOneByIdAndUid(Long id, Long uid) {
        ActivityCheckIn checkIn = findOne(id);
        if (checkIn == null) return null;
        if (checkIn.getUid() == null || checkIn.getUid() != uid) return null;
        return checkIn;
    }

    @Override
    public ActivityCheckIn findOneByActivityIdAndUid(Long activityId, Long uid) {
        return repository.findByActivityIdAndUid(activityId, uid);
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
    public ActivityCheckIn save(@Validated ActivityCheckIn activityCheckIn) throws ResourceAlreadyExistedException {
        if (null == findOneByActivityIdAndUid(activityCheckIn.getActivityId(), activityCheckIn.getUid()))
            return repository.save(activityCheckIn);
        throw new ResourceAlreadyExistedException("您已经报过名了");
    }

    @Override
    public ActivityCheckIn updateIfNotExisted(ActivityCheckIn activityCheckIn) {
        return repository.save(activityCheckIn);
    }
}
