package com.seeu.ywq.userlogin.service;

import java.util.Date;

/**
 * 登陆日志
 */
public interface UserLoginLogService {
    void updateLog(Long uid, String ip, Date time);
}
