package com.seeu.ywq.userlogin.service;

import com.seeu.ywq.userlogin.model.UserVIP;

public interface UserVIPService {
    UserVIP findOne(Long uid);

    UserVIP findOneIfActive(Long uid);

    UserVIP save(UserVIP userVIP);
}
