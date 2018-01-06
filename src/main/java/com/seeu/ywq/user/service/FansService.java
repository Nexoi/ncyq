package com.seeu.ywq.user.service;

import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.user.model.Fans;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FansService {

    boolean hasFollowedHer(Long myUid, Long herUid);

    Page findPageByFansUid(Long fansUid, Pageable pageable);

    Page findPageByFollowedUid(Long followedUid, Pageable pageable);

    List<Fans> findAllByFansUid(Long uid);

    void followSomeone(Long myUid, Long herUid) throws NoSuchUserException, ActionNotSupportException;

    void cancelFollowSomeone(Long myUid, Long herUid) throws NoSuchUserException, ActionNotSupportException;

}
