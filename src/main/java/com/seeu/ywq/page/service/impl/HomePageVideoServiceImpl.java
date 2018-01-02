package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.model.HomePageVideo;
import com.seeu.ywq.page.repository.HomePageVideoRepository;
import com.seeu.ywq.page.service.HomePageVideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HomePageVideoServiceImpl implements HomePageVideoService {
    @Resource
    private HomePageVideoRepository repository;

    @Override
    public HomePageVideo findOne(Long videoId) {
        HomePageVideo video = repository.findOne(videoId);
        if (video != null) repository.viewItOnce(videoId);
        return video;
    }

    @Override
    public Page findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUid(uid, pageable);
    }
}
