package com.seeu.ywq.uservip.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
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
        if (uid == null) return null;
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
    public UserVIP active(Long uid, Long day) {
        UserVIP vip = findOne(uid);
        Date date = new Date();
        if (vip == null) {
            vip = new UserVIP();
            vip.setVipLevel(UserVIP.VIP.none);
            vip.setTerminationDate(date);
            vip.setUid(uid);
        }
        if (vip.getTerminationDate() == null || vip.getTerminationDate().before(date))
            vip.setTerminationDate(date);
        vip.setVipLevel(UserVIP.VIP.vip);
        vip.setUpdateTime(date);
        vip.setTerminationDate(new Date(vip.getTerminationDate().getTime() + day * 24 * 60 * 60 * 1000));
        return save(vip);
    }

    @Override
    public UserVIP disable(Long uid) throws ResourceNotFoundException {
        UserVIP vip = findOne(uid);
        if (vip == null) throw new ResourceNotFoundException("找不到用户VIP信息");
        vip.setTerminationDate(new Date());
        vip.setVipLevel(UserVIP.VIP.none);
        vip.setUpdateTime(new Date());
        return save(vip);
    }

    @Override
    public UserVIP save(UserVIP userVIP) {
        return repository.save(userVIP);
    }
}
