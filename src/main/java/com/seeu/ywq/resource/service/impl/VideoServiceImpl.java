package com.seeu.ywq.resource.service.impl;

import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.resource.repository.VideoRepository;
import com.seeu.ywq.resource.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoRepository repository;

    @Override
    public Video save(Video video) {
        return repository.save(video);
    }
}
