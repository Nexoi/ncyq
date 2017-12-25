package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.dvo.apppage.PublishLiteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublishLiteService {
    Page<PublishLiteVO> findAllByTagIds(Long visitorUid, Pageable pageable, Long... ids);

    Page<PublishLiteVO> findAll(Long visitorUid, Pageable pageable);

    Page<PublishLiteVO> findAllByFollowedUids(Long visitorUid, Pageable pageable, Long... uids);

    Page<PublishLiteVO> findAllByUid(Long uid, Pageable pageable);
}
