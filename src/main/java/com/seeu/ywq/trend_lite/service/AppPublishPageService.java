package com.seeu.ywq.trend_lite.service;

import com.seeu.ywq.trend_lite.dvo.PublishLiteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppPublishPageService {

    Page<PublishLiteVO> getTuijian(Long uid, Pageable pageable);

    Page<PublishLiteVO> getGuanzhu(Long uid, Pageable pageable);

    Page<PublishLiteVO> getWhose(Long visitorUid, Long uid, Pageable pageable);
}
