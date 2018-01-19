package com.seeu.ywq.trend.service;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.trend.model.Picture;

import java.util.List;

public interface PublishPictureService {
    List<Picture> save(List<Picture> pictures);

    Image getCoverOpen(Long uid);

    Image getCoverClose(Long uid);
}
