package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.UserVIP;
import com.seeu.ywq.userlogin.repository.UserVIPRepository;
import com.seeu.ywq.userlogin.service.UserVIPService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserVIPServiceImpl implements UserVIPService {
    @Resource
    private UserVIPRepository repository;

    @Override
    public UserVIP findOne(Long uid) {
        return repository.findOne(uid);
    }

    @Override
    public UserVIP findOneIfActive(Long uid) {
        UserVIP vip = repository.findOne(uid);
        if (vip == null) return null;
        Date terDate = vip.getTerminationDate();
        if (terDate == null)
            return null;
        if (terDate.before(new Date())) // 已经过期
            return null;
        if (vip.getVipLevel() == null || vip.getVipLevel() == UserVIP.VIP.none)
            return null;
        return vip;
    }

    @Override
    public UserVIP save(UserVIP userVIP) {
        return repository.save(userVIP);
    }
}
