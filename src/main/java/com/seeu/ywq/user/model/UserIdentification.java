package com.seeu.ywq.user.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户认证
 */
@Entity
@IdClass(UserIdentificationPKeys.class)
@Table(name = "ywq_user_identifications", indexes = {
        @Index(name = "identification_index1", unique = false, columnList = "uid"),
        @Index(name = "identification_index1", unique = false, columnList = "identification_id"),
        @Index(name = "identification_index3", unique = false, columnList = "status")
})
public class UserIdentification {
    public enum STATUS {
        pass,    // 通过
        nonactive,
        waitFor, // 待审核
        failure  // 审核失败
    }

    @Id
    @Column(name = "identification_id", unique = false)
    private Long identificationId;
    @Id
    @Column(name = "uid", unique = false)
    private Long uid;

    private STATUS status; // 认证状态

    private Date createTime;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
