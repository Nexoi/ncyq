package com.seeu.ywq.resource.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户是否有权限查看该资源（一般建议原子粒度为：动态以上）内容照片
 */
@Entity
@IdClass(ResourceAuthPKeys.class)
@Table(name = "ywq_resources_unlock")
public class ResourceAuth {
    @Id
    @Column(name = "resource_id")
    private Long resourceId;
    @Id
    @Column(name = "uid")
    private Long uid;

    private Date outTime; // 过期时间

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
