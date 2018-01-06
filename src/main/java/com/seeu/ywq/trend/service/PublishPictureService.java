package com.seeu.ywq.trend.service;

import com.seeu.ywq.resource.model.Image;

public interface PublishPictureService {
    Image getCoverOpen(Long uid);

    Image getCoverClose(Long uid);
}
