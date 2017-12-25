package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.dvo.apppage.PublishLiteVO;
import com.seeu.ywq.release.model.apppage.Advertisement;
import com.seeu.ywq.release.model.apppage.PublishLite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppPublishPageService {
    List<Advertisement> getPublishPageAdvertisements();

    Page<PublishLiteVO> getTuijian(Long uid, Pageable pageable);

    Page<PublishLiteVO> getGuanzhu(Long uid, Pageable pageable);

    Page<PublishLiteVO> getWhose(Long uid, Pageable pageable);
}
