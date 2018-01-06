package com.seeu.ywq.trend.service.impl;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.trend.repository.PublishPictureRepository;
import com.seeu.ywq.trend.service.PublishPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PublishPictureServiceImpl implements PublishPictureService {
    @Resource
    private PublishPictureRepository publishPictureRepository;


    @Override
    public Image getCoverOpen(Long uid) {
        Picture picture = publishPictureRepository.findFirstByUidAndAlbumTypeAndDeleteFlagOrderByCreateTimeDesc(uid, Picture.ALBUM_TYPE.open, Picture.DELETE_FLAG.show);
        if (picture == null) return null;
        return picture.getImageOpen();
    }

    @Override
    public Image getCoverClose(Long uid) {
        Picture picture = publishPictureRepository.findFirstByUidAndAlbumTypeAndDeleteFlagOrderByCreateTimeDesc(uid, Picture.ALBUM_TYPE.close, Picture.DELETE_FLAG.show);
        if (picture == null) return null;
        return picture.getImageOpen();
    }
}
