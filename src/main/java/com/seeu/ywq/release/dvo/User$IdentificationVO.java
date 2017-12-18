package com.seeu.ywq.release.dvo;

import com.seeu.ywq.release.model.User$Identification;
import com.seeu.ywq.release.model.User$IdentificationPKeys;

import javax.persistence.*;
import java.util.Date;

public class User$IdentificationVO {

    private Long identificationId;

    private User$Identification.STATUS status; // 认证状态

    private Date createTime;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public User$Identification.STATUS getStatus() {
        return status;
    }

    public void setStatus(User$Identification.STATUS status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
