package com.seeu.ywq.photography.service;

import com.seeu.ywq.photography.model.PhotographyDay;

public interface PhotographyDayService {
    PhotographyDay findByUid(Long uid);

    PhotographyDay save(PhotographyDay photographyDay);
}
