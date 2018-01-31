package com.seeu.ywq.trend_lite.service;

import com.seeu.ywq.trend_lite.dvo.PublishLiteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublishLiteService {
    /**
     * 使用范围：推荐
     *
     * @param visitorUid
     * @param pageable
     * @param ids
     * @return
     */
    Page<PublishLiteVO> findAllByTagIds(Long visitorUid, Pageable pageable, Long... ids);

    /**
     * 使用范围：关注
     *
     * @param visitorUid
     * @param pageable
     * @param uids
     * @return
     */
    Page<PublishLiteVO> findAllByFollowedUids(Long visitorUid, Pageable pageable, Long... uids);

    /**
     * 使用范围：我的动态／她的动态
     *
     * @param visitorUid
     * @param uid
     * @param pageable
     * @return
     */
    Page<PublishLiteVO> findAllByUid(Long visitorUid, Long uid, Pageable pageable);


    Page<PublishLiteVO> search(Long visitorUid, String word, Pageable pageable);

}
