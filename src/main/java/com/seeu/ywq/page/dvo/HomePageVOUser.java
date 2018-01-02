package com.seeu.ywq.page.dvo;

import com.seeu.ywq.resource.model.Image;

import java.util.List;

public class HomePageVOUser {

    private Long uid;
    //    private Long fansNum;   // 粉丝数
    //    private Long followNum; // 关注人数
    private Long likeNum;   // 点赞人数

    private List<Long> identifications;

    private Image coverImage; // 封面图片

    // 从其他地方注入进来
    private String nickname;  //
    private String headIconUrl;

    ////////

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<Long> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Long> identifications) {
        this.identifications = identifications;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
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
}
