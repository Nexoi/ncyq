package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.UserIdentification;

import javax.persistence.Column;
import java.util.Date;

public class UserIdentificationWithFullListVO {
    private Long identificationId;
    @Column(length = 20)
    private String identificationName;
    private String iconUrl;
    private String iconActiveUrl;
    private UserIdentification.STATUS status; // 认证状态
    private Date createTime;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public String getIdentificationName() {
        return identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconActiveUrl() {
        return iconActiveUrl;
    }

    public void setIconActiveUrl(String iconActiveUrl) {
        this.iconActiveUrl = iconActiveUrl;
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
