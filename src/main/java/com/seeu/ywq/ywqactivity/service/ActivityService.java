package com.seeu.ywq.ywqactivity.service;

import com.seeu.ywq.ywqactivity.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {
    Activity save(Activity activity);

    Activity findOne(Long id);

    List<Activity> findTop6();

    Page<Activity> findAll(Pageable pageable);
}
