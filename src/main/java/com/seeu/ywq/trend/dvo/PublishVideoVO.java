package com.seeu.ywq.trend.dvo;

import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.model.PublishVideo;

import java.util.Date;

public class PublishVideoVO {
    private Long id;
    private Date createTime;//创建时间
    private PublishVideo.VIDEO_TYPE videoType;
    private Video video;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PublishVideo.VIDEO_TYPE getVideoType() {
        return videoType;
    }

    public void setVideoType(PublishVideo.VIDEO_TYPE videoType) {
        this.videoType = videoType;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
