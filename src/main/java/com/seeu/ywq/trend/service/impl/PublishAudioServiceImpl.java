package com.seeu.ywq.trend.service.impl;

import com.seeu.ywq.trend.model.PublishAudio;
import com.seeu.ywq.trend.repository.PublishAudioRepository;
import com.seeu.ywq.trend.service.PublishAudioService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PublishAudioServiceImpl implements PublishAudioService {
    @Resource
    private PublishAudioRepository repository;

    @Override
    public PublishAudio save(PublishAudio audio) {
        return repository.save(audio);
    }
}
