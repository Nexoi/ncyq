package com.seeu.ywq.release.service.apppage.impl;

import com.seeu.ywq.release.model.apppage.HomePageVideo;
import com.seeu.ywq.release.repository.apppage.HomePageVideoRepository;
import com.seeu.ywq.release.service.apppage.HomePageVideoService;
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
