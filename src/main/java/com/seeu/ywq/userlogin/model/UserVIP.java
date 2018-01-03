package com.seeu.ywq.userlogin.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ywq_user_vip")
public class UserVIP {
    public enum VIP {
        none,
        vip
    }

    @Id
    private Long uid;
    private VIP vipLevel;
    private Date terminationDate;
    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public VIP getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(VIP vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
