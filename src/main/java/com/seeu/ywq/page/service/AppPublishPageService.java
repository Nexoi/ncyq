package com.seeu.ywq.page.service;

import com.seeu.ywq.page.dvo.PublishLiteVO;
import com.seeu.ywq.page.model.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppPublishPageService {
    List<Advertisement> getPublishPageAdvertisements();

    Page<PublishLiteVO> getTuijian(Long uid, Pageable pageable);

    Page<PublishLiteVO> getGuanzhu(Long uid, Pageable pageable);

    Page<PublishLiteVO> getWhose(Long visitorUid, Long uid, Pageable pageable);
}
