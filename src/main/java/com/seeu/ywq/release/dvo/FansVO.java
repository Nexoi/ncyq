package com.seeu.ywq.release.dvo;

import com.seeu.ywq.release.model.Fans;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.util.List;

public class FansVO {
    @Column(name = "fans_uid")
    private Long fansUid;
    @Column(name = "followed_uid")
    private Long followedUid;
    @Column(name = "nickname")
    @JoinColumn(name = "nickname", referencedColumnName = "nickname", table = "UserLogin")
    private String nickname;
    @Column(name = "head_icon_url")
    private String headIconUrl;
    @Column(name = "text")
    private String text;
    @Column(name = "identification_ids")
    private List<Long> identificationIds;
    @Column(name = "follow_each")
    private Fans.FOLLOW_EACH followEach;

    public FansVO() {
    }

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Long getFollowedUid() {
        return followedUid;
    }

    public void setFollowedUid(Long followedUid) {
        this.followedUid = followedUid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Long> getIdentificationIds() {
        return identificationIds;
    }

    public void setIdentificationIds(List<Long> identificationIds) {
        this.identificationIds = identificationIds;
    }

    public Fans.FOLLOW_EACH getFollowEach() {
        return followEach;
    }

    public void setFollowEach(Fans.FOLLOW_EACH followEach) {
        this.followEach = followEach;
    }
}
