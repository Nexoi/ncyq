package com.seeu.ywq.release.model;

import javax.persistence.*;

/**
 * 用户是否有权限查看该动态内容照片
 */
@Entity
@IdClass(Publish$UserKeys.class)
@Table(name = "ywq_publish_unlock_users")
public class Publish$User {
    @Id
    @Column(name = "publish_id")
    private Long publishId;
    @Id
    @Column(name = "uid")
    private Long uid;

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
