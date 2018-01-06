package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.Fans;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.util.List;

public class FansVO {
    @Column(name = "fans_uid")
    private Long fansUid;
    @Column(name = "followed_uid")
    private Long followedUid;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "head_icon_url")
    private String headIconUrl;
    @Column(name = "introduce")
    private String introduce;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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
