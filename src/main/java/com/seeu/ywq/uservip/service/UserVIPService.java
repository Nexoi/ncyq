package com.seeu.ywq.uservip.service;

import com.seeu.ywq.uservip.model.UserVIP;

public interface UserVIPService {
    UserVIP findOne(Long uid);

    UserVIP findOneIfActive(Long uid);

    boolean isActive(Long uid);

    UserVIP save(UserVIP userVIP);
}
