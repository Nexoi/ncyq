package com.seeu.ywq.share.service;

import com.seeu.ywq.share.model.SharePicture;

import java.util.List;

public interface SharePicturesService {
    SharePicture save(SharePicture sharePicture);

    SharePicture findOne(Long id);

    List<SharePicture> findAll();

    void delete(Long id);
}
