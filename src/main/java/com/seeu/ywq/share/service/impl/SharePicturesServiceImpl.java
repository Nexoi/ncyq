package com.seeu.ywq.share.service.impl;

import com.seeu.ywq.share.model.SharePicture;
import com.seeu.ywq.share.repository.SharePicturesRepository;
import com.seeu.ywq.share.service.SharePicturesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SharePicturesServiceImpl implements SharePicturesService {

    @Resource
    private SharePicturesRepository repository;

    @Override
    public SharePicture save(SharePicture sharePicture) {
        return repository.save(sharePicture);
    }

    @Override
    public SharePicture findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<SharePicture> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
