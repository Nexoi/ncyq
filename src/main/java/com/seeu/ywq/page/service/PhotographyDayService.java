package com.seeu.ywq.page.service;

import com.seeu.ywq.page.model.PhotographyDay;

public interface PhotographyDayService {
    PhotographyDay findByUid(Long uid);

    PhotographyDay save(PhotographyDay photographyDay);
}
