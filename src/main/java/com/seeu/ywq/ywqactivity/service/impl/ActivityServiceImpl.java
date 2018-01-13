package com.seeu.ywq.ywqactivity.service.impl;

import com.seeu.ywq.ywqactivity.model.Activity;
import com.seeu.ywq.ywqactivity.repository.ActivityRepository;
import com.seeu.ywq.ywqactivity.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityRepository repository;

    @Override
    public Activity save(Activity activity) {
        return repository.save(activity);
    }

    @Override
    public Activity findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Activity> findTop6() {
        return repository.findTop6ByDeleteFlag(Activity.DELETE_FLAG.show);
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return repository.findAllByDeleteFlag(Activity.DELETE_FLAG.show, pageable);
    }
}
