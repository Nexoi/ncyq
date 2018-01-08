package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.model.PhotographyDay;
import com.seeu.ywq.page.repository.PhotographyDayRepository;
import com.seeu.ywq.page.service.PhotographyDayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PhotographyDayServiceImpl implements PhotographyDayService {
    @Resource
    private PhotographyDayRepository repository;

    @Override
    public PhotographyDay findByUid(Long uid) {
        return repository.findOne(uid);
    }

    @Override
    public PhotographyDay save(PhotographyDay photographyDay) {
        return repository.save(photographyDay);
    }
}
