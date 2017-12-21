package com.seeu.ywq.release.dvo.apppage;

import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.ApiParam;

import javax.persistence.Column;
import javax.persistence.Enumerated;

public class SimpleUserVO {
    private Long uid;
    private String nickname;
    private UserLogin.GENDER gender;
    private String headIconUrl;
    private Boolean followed;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserLogin.GENDER getGender() {
        return gender;
    }

    public void setGender(UserLogin.GENDER gender) {
        this.gender = gender;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }
}
