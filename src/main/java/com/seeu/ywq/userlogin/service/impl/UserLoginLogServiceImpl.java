package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserLoginLogService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Resource
    private UserReactService userReactService;

    @Override
    public void updateLog(Long uid, String ip, Date time) {
        UserLogin ul = userReactService.findOne(uid);
        if (uid == null) return;
        ul.setLastLoginIp(ip);
        ul.setLastLoginTime(time);
        userReactService.save(ul);
    }
}
