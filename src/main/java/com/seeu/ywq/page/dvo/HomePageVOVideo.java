package com.seeu.ywq.page.dvo;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.user.dvo.SimpleUserVO;

import java.util.Date;

public class HomePageVOVideo {
    private Long id;
    private HomePageVideo.CATEGORY category;
    private String title;
    private Long uid;
    private String nickname;
    private String headIconUrl;
    private Long viewNum;
    private Date createTime;
    private Image coverImage;
    private Video video;
    // ...
    private SimpleUserVO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HomePageVideo.CATEGORY getCategory() {
        return category;
    }

    public void setCategory(HomePageVideo.CATEGORY category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getViewNum() {
        return viewNum;
    }

    public void setViewNum(Long viewNum) {
        this.viewNum = viewNum;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
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

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SimpleUserVO getUser() {
        return user;
    }

    public void setUser(SimpleUserVO user) {
        this.user = user;
    }
}
