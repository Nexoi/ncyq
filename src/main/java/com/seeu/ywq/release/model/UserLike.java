package com.seeu.ywq.release.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

@Entity
@IdClass(UserLikePKeys.class)
@Table(name = "ywq_user_like")
public class UserLike {
    @Id
    private Long uid;
    @Id
    private Long likedUid;
    private Date createTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getLikedUid() {
        return likedUid;
    }

    public void setLikedUid(Long likedUid) {
        this.likedUid = likedUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
