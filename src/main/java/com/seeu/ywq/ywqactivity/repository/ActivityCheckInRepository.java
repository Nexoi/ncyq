package com.seeu.ywq.ywqactivity.repository;

import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ActivityCheckInRepository extends JpaRepository<ActivityCheckIn, Long> {

    Page<ActivityCheckIn> findAllByUid(@Param("uid") Long uid, Pageable pageable);

    Page<ActivityCheckIn> findAllByActivityId(@Param("activityId") Long activityId, Pageable pageable);

}
