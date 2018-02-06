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
    public boolean canVisitPublish(Long uid, Long publishId, Date currentTime) {
        if (uid == null) return false;
        return userVIPService.isActive(uid) || // 会员全部放行
                0 != resourceAuthRepository.countAllByUidAndTypeAndResourceIdAndOutTimeAfter(uid, ResourceAuth.TYPE.publish, publishId, currentTime);
    }

    @Override
    public boolean canVisitPublish(Long uid, Long resourceId) {
        return canVisitPublish(uid, resourceId, new Date());
    }

    @Override
    public void activePublishResource(Long uid, Long resourceId, Integer day) {
        if (day == null || day <= 0) return;
        ResourceAuth resourceAuth = resourceAuthRepository.findOne(new ResourceAuthPKeys(uid, ResourceAuth.TYPE.publish, resourceId));
        if (resourceAuth == null) {
            resourceAuth = new ResourceAuth();
            resourceAuth.setUid(uid);
            resourceAuth.setType(ResourceAuth.TYPE.publish);
            resourceAuth.setResourceId(resourceId);
            resourceAuth.setOutTime(new Date()); // 初始化为当前时间
        }
        // 激活时间
        Date date = new Date();
        date.setTime(date.getTime() + day * 24 * 60 * 60 * 1000);
        resourceAuth.setOutTime(date);
        resourceAuthRepository.save(resourceAuth);
    }

    @Override
    public boolean canVisitVideo(Long uid, Long videoId, Date currentTime) {
        return true; // update 2018-02-06 暂时全部放行
//        if (uid == null) return false;
//        return userVIPService.isActive(uid) // 会员全部放行
//                || 0 != resourceAuthRepository.countAllByUidAndTypeAndResourceIdAndOutTimeAfter(uid, ResourceAuth.TYPE.video, videoId, currentTime);
    }

    @Override
    public boolean canVisitVideo(Long uid, Long videoId) {
        return canVisitVideo(uid, videoId, new Date());
    }

    @Override
    public void activeVideoResource(Long uid, Long videoId, Integer day) {
        if (day == null || day <= 0) return;
        ResourceAuth resourceAuth = resourceAuthRepository.findOne(new ResourceAuthPKeys(uid, ResourceAuth.TYPE.video, videoId));
        if (resourceAuth == null) {
            resourceAuth = new ResourceAuth();
            resourceAuth.setUid(uid);
            resourceAuth.setType(ResourceAuth.TYPE.video);
            resourceAuth.setResourceId(videoId);
            resourceAuth.setOutTime(new Date()); // 初始化为当前时间
        }
        // 激活时间（當前時期 + day 天）
        Date date = new Date();
        date.setTime(date.getTime() + day * 24 * 60 * 60 * 1000);
        resourceAuth.setOutTime(date);
        resourceAuthRepository.save(resourceAuth);
    }
}
