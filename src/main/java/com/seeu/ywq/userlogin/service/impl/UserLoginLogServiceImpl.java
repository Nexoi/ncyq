package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import com.seeu.ywq.userlogin.service.UserLoginLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Resource
    private UserLoginRepository userLoginRepository;

    @Override
    public void updateLog(Long uid, String ip, Date time) {
        UserLogin ul = userLoginRepository.findOne(uid);
        if (uid == null) return;
        ul.setLastLoginIp(ip);
        ul.setLastLoginTime(time);
        userLoginRepository.saveAndFlush(ul);
    }
}
