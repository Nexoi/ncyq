package com.seeu.ywq.uservip.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.model.UserVIP;

public interface UserVIPService {
    UserVIP findOne(Long uid);

    UserVIP findOneIfActive(Long uid);

    boolean isActive(Long uid);

    // 激活
    UserVIP active(Long uid, Long day);

    // 注销
    UserVIP disable(Long uid) throws ResourceNotFoundException;

    UserVIP save(UserVIP userVIP);
}
