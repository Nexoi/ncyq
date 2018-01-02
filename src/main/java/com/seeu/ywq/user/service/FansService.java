package com.seeu.ywq.user.service;

import com.seeu.ywq.user.model.Fans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FansService {

    boolean hasFollowedHer(Long myUid, Long herUid);

    Page findPageByFansUid(Long fansUid, Pageable pageable);

    Page findPageByFollowedUid(Long followedUid, Pageable pageable);

    List<Fans> findAllByFansUid(Long uid);

    STATUS followSomeone(Long myUid, Long hisUid);

    public enum STATUS {
        not_such_person,
        have_followed,
        success,
        failure
    }
}
