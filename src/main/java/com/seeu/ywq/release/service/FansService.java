package com.seeu.ywq.release.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FansService {

    Page findPageByFansUid(Long fansUid, Pageable pageable);

    Page findPageByFollowedUid(Long followedUid, Pageable pageable);

    STATUS followSomeone(Long myUid, Long hisUid);

    public enum STATUS{
        not_such_person,
        have_followed,
        success,
        failure
    }
}
