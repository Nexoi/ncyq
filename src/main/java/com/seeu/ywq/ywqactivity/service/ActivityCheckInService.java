package com.seeu.ywq.ywqactivity.service;

import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityCheckInService {
    ActivityCheckIn findOne(Long id);

    ActivityCheckIn findOneByIdAndUid(Long id, Long uid);

    ActivityCheckIn findOneByActivityIdAndUid(Long activityId, Long uid);

    Page<ActivityCheckIn> findAllByActivityId(Long activityId, Pageable pageable);

    Page<ActivityCheckIn> findAllByUid(Long uid, Pageable pageable);

    Page<ActivityCheckIn> findAll(Pageable pageable);

    ActivityCheckIn save(ActivityCheckIn activityCheckIn) throws ResourceAlreadyExistedException;

    ActivityCheckIn updateIfExisted(ActivityCheckIn activityCheckIn);
}
