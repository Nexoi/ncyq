package com.seeu.ywq.uservip.service.impl;

import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.repository.UserVIPRepository;
import com.seeu.ywq.uservip.service.UserVIPService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserVIPServiceImpl implements UserVIPService {
    @Resource
    private UserVIPRepository repository;

    @Override
    public UserVIP findOne(Long uid) {
//        UserVIP vip = repository.findOne(uid);
//        不需要轉化成非會員信息
//        if (null != vip && null != vip.getTerminationDate()) {
//            if (vip.getTerminationDate().before(new Date()))
//                vip.setVipLevel(UserVIP.VIP.none);
//        }
        return repository.findOne(uid);
    }

    @Override
    public UserVIP findOneIfActive(Long uid) {
        UserVIP vip = findOne(uid);
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
    public boolean isActive(Long uid) {
        if (uid == null) return false;
        UserVIP vip = repository.findOne(uid);
        return !(vip == null
                || vip.getVipLevel() == null
                || vip.getVipLevel() == UserVIP.VIP.none
                || vip.getTerminationDate() == null
                || vip.getTerminationDate().before(new Date()));
    }

    @Override
    public UserVIP save(UserVIP userVIP) {
        return repository.save(userVIP);
    }
}
