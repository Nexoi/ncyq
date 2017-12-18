package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.UserVO;

public interface UserInfoService {
    UserVO findOne(Long uid);
}
