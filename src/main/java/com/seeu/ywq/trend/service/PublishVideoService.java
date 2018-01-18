package com.seeu.ywq.trend.service;

import com.seeu.ywq.trend.dvo.PublishVideoVO;
import com.seeu.ywq.trend.model.PublishVideo;

public interface PublishVideoService {
    PublishVideo save(PublishVideo publishVideo);

    PublishVideoVO transferToVO(PublishVideo publishVideo, boolean canVisitClosedResource);
}
