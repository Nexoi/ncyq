package com.seeu.ywq.page.dvo;

import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.uservip.model.UserVIP;

import java.util.List;

public class SimpleUserVO {
    private Long uid;
    private String nickname;
    private UserLogin.GENDER gender;
    private String headIconUrl;
    private Boolean followed;
    private Boolean liked;
    private UserVIP.VIP vip; // 存储 vip 等级信息
    private List<Long> identifications;

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

    public List<Long> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Long> identifications) {
        this.identifications = identifications;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public UserVIP.VIP getVip() {
        return vip;
    }

    public void setVip(UserVIP.VIP vip) {
        this.vip = vip;
    }
}
