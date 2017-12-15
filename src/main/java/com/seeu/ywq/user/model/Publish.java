package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "publish")
public class Publish {

    public enum PUBLISH_TYPE {
        word,
        picture,
        video
    }

    public enum PUBLIC_STATUS {
        normal,
        block
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @ApiParam(hidden = true)
    private Long uid;//用户账号

    @NotNull
    @Enumerated
    private PUBLISH_TYPE type;//发布类型，1表示文字，2表示图片，3表示视屏，4表示拍摄

    @NotNull
    private String title;//标题

    @ApiParam(hidden = true)
    private Date createTime;//创建时间

    private BigDecimal unlockPrice;//解锁需要金额
    @Enumerated
    private PUBLIC_STATUS state;//状态，正常/封禁

    @ApiParam(hidden = true)
    private Integer viewNum; // 浏览次数

    @ApiParam(hidden = true)
    private Integer commentNum;//留言数(不与数据库做交互)

    @ApiParam(hidden = true)
    private Integer likeNum;//点赞数(不与数据库做交互)

    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LikedUser> likedUserList; // 点赞人列表

    private String labels;//标签（逗号隔开）

    @Column(length = Integer.MAX_VALUE)
    private String text;    // 文本内容

    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Picture> pictures;//图片(不与数据库做交互)

    private String coverVideoUrl;

    private String videoUrls;//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public PUBLISH_TYPE getType() {
        return type;
    }

    public void setType(PUBLISH_TYPE type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PUBLIC_STATUS getState() {
        return state;
    }

    public void setState(PUBLIC_STATUS state) {
        this.state = state;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public List<LikedUser> getLikedUserList() {
        return likedUserList;
    }

    public void setLikedUserList(List<LikedUser> likedUserList) {
        this.likedUserList = likedUserList;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public String getCoverVideoUrl() {
        return coverVideoUrl;
    }

    public void setCoverVideoUrl(String coverVideoUrl) {
        this.coverVideoUrl = coverVideoUrl;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }
}
