package com.seeu.ywq.user.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 关注/粉丝表
 */
@Entity
@IdClass(FansPKeys.class)
@Table(name = "ywq_fans", indexes = {
        @Index(name = "fans_index1", unique = false, columnList = "fans_uid"),
        @Index(name = "fans_index1", unique = false, columnList = "followed_uid")
})
public class Fans {

    public enum FOLLOW_EACH {
        none,
        single,
        each
    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @NotNull
    @Id
    @Column(name = "fans_uid", unique = false)
    private Long fansUid;//用户账号

    @NotNull
    @Id
    @Column(name = "followed_uid", unique = false)
    private Long followedUid;//被关注的用户账号

    @Enumerated
    private FOLLOW_EACH followEach;

    private DELETE_FLAG deleteFlag;

    private Date createTime;//创建时间

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Long getFollowedUid() {
        return followedUid;
    }

    public void setFollowedUid(Long followedUid) {
        this.followedUid = followedUid;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public FOLLOW_EACH getFollowEach() {
        return followEach;
    }

    public void setFollowEach(FOLLOW_EACH followEach) {
        this.followEach = followEach;
    }
}
