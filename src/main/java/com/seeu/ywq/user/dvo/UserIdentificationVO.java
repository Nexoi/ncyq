package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.UserIdentification;

import java.util.Date;

public class UserIdentificationVO {

    private Long identificationId;

    private IdentificationApply.STATUS status; // 认证状态

    private Date createTime;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
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
