package com.seeu.ywq.release.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ywq_publish_video")
public class PublishVideo {
    public enum VIDEO_TYPE {
        open,
        close
    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @ApiParam(hidden = true)
    @NotNull
    private Long uid;

    @ApiParam(hidden = true)
    @Column(name = "publish_id")
    private Long publishId; //发布动态的 id


    @ApiParam(hidden = true)
    @Enumerated
    private VIDEO_TYPE videoType;

    @ApiParam(hidden = true)
    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video video;


    @ApiParam(hidden = true)
    private Date createTime;//创建时间

    @ApiParam(hidden = true)
    private Date deleteTime;//删除时间

    @ApiParam(hidden = true)
    private DELETE_FLAG deleteFlag;//删除标记

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public VIDEO_TYPE getVideoType() {
        return videoType;
    }

    public void setVideoType(VIDEO_TYPE videoType) {
        this.videoType = videoType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
