package com.seeu.ywq.trend.service.impl;

import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.dvo.PublishVideoVO;
import com.seeu.ywq.trend.model.PublishVideo;
import com.seeu.ywq.trend.repository.PublishVideoRepository;
import com.seeu.ywq.trend.service.PublishVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PublishVideoServiceImpl implements PublishVideoService {
    @Resource
    private PublishVideoRepository repository;

    @Override
    public PublishVideo save(PublishVideo publishVideo) {
        return repository.save(publishVideo);
    }

    @Override
    public PublishVideoVO transferToVO(PublishVideo publishVideo, boolean canVisitClosedResource) {
        if (publishVideo == null) return null;
        PublishVideoVO vo = new PublishVideoVO();
        BeanUtils.copyProperties(publishVideo, vo);
        if (publishVideo.getVideo() == null) return vo;
        Video video = publishVideo.getVideo();
        if (!canVisitClosedResource) {
            video.setSrcUrl(null);
            vo.setVideoType(PublishVideo.VIDEO_TYPE.close);
        } else {
            vo.setVideoType(PublishVideo.VIDEO_TYPE.open);
        }
        // TODO 完成视频权限机制
        return vo;
    }
}
