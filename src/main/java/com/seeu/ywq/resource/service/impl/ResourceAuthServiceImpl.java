package com.seeu.ywq.resource.service.impl;

import com.seeu.ywq.resource.model.ResourceAuth;
import com.seeu.ywq.resource.model.ResourceAuthPKeys;
import com.seeu.ywq.resource.repository.ResourceAuthRepository;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.uservip.service.UserVIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ResourceAuthServiceImpl implements ResourceAuthService {
    @Resource
    private ResourceAuthRepository resourceAuthRepository;
    @Autowired
    private UserVIPService userVIPService;

    @Override
    public boolean canVisit(Long uid, Long publishId, Date currentTime) {
        return userVIPService.isActive(uid) || // 会员全部放行
                0 != resourceAuthRepository.countAllByUidAndResourceIdAndOutTimeAfter(uid, publishId, currentTime);
    }

    @Override
    public boolean canVisit(Long uid, Long resourceId) {
        return canVisit(uid, resourceId, new Date());
    }

    @Override
    public void activeResource(Long uid, Long resourceId, Integer day) {
        if (day == null || day <= 0) return;
        ResourceAuth resourceAuth = resourceAuthRepository.findOne(new ResourceAuthPKeys(uid, resourceId));
        if (resourceAuth == null) {
            resourceAuth = new ResourceAuth();
            resourceAuth.setUid(uid);
            resourceAuth.setResourceId(resourceId);
            resourceAuth.setOutTime(new Date()); // 初始化为当前时间
        }
        // 激活时间
        Date date = new Date();
        date.setTime(date.getTime() + day * 24 * 60 * 60 * 1000);
        resourceAuth.setOutTime(date);
        resourceAuthRepository.save(resourceAuth);
    }
}
