package com.seeu.ywq.user.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户标签
 */
@Entity
@IdClass(User$TagPKeys.class)
@Table(name = "ywq_user_tags")
public class User$Tag {
    @Id
    @Column(name = "user_uid", unique = false)
    private Long uid;
    @Id
    @Column(name = "tags_id", unique = false)
    private Long tagId;


    @Column(name = "create_time")
    private Date createTime;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
