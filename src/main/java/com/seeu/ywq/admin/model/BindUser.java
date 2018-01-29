package com.seeu.ywq.admin.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:14 PM
 * Describe:
 */

@Entity
@Table(name = "ywq_admin_bind_users")
public class BindUser {

    @Id
    private Long nickUid;

    @NotNull
    private Long adminUid; // 管理者

    private Date createTime;

    public Long getNickUid() {
        return nickUid;
    }

    public void setNickUid(Long nickUid) {
        this.nickUid = nickUid;
    }

    public Long getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(Long adminUid) {
        this.adminUid = adminUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
