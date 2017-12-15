package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_login")
public class LikedUser {

    @ApiParam(hidden = true)
    @Id
    private Long uid;

    @ApiParam(hidden = true)
    private String headIconUrl;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }
}
