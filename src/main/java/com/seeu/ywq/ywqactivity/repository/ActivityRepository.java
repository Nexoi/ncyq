package com.seeu.ywq.ywqactivity.repository;

import com.seeu.ywq.ywqactivity.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop6ByDeleteFlag(@Param("deleteFlag") Activity.DELETE_FLAG deleteFlag);

    Page<Activity> findAllByDeleteFlag(@Param("deleteFlag") Activity.DELETE_FLAG deleteFlag, Pageable pageable);
}
