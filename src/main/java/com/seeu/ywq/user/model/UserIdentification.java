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

    @Id
    @Column(name = "identification_id", unique = false)
    private Long identificationId;
    @Id
    @Column(name = "uid", unique = false)
    private Long uid;

    @Enumerated
    private IdentificationApply.STATUS status; // 认证状态

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

    public IdentificationApply.STATUS getStatus() {
        return status;
    }

    public void setStatus(IdentificationApply.STATUS status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
