package com.seeu.ywq.release.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 男，专用
 */
@Entity
@IdClass(User$TagPKeys.class)
@Deprecated
@Table(name = "ywq_tag_with_user_focus")
public class Tag$UserFocus {
    @Id
    private Long tagId;
    @Id
    private Long uid;

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
