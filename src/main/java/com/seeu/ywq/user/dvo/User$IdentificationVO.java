package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.UserIdentification;

import java.util.Date;

public class User$IdentificationVO {

    private Long identificationId;

    private UserIdentification.STATUS status; // 认证状态

    private Date createTime;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public UserIdentification.STATUS getStatus() {
        return status;
    }

    public void setStatus(UserIdentification.STATUS status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
