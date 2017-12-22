package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.model.ResourceAuth;
import com.seeu.ywq.release.model.ResourceAuthPKeys;
import com.seeu.ywq.release.repository.ResourceAuthRepository;
import com.seeu.ywq.release.service.ResourceAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ResourceAuthServiceImpl implements ResourceAuthService {
    @Resource
    private ResourceAuthRepository resourceAuthRepository;

    @Override
    public boolean canVisit(Long uid, Long publishId, Date currentTime) {
        return 0 != resourceAuthRepository.countAllByUidAndResourceIdAndOutTimeAfter(uid, publishId, currentTime);
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
