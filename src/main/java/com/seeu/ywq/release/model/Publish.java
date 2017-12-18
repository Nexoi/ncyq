package com.seeu.ywq.release.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ywq_publish")
public class Publish {

    public enum PUBLISH_TYPE {
        word,
        picture,
        video
    }

    public enum STATUS {
        normal,
        block,
        delete
    }

    public enum SRC_TYPE {
        open,
        close
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @ApiParam(hidden = true)
    private Long uid;//用户账号

    @ApiParam(hidden = true)
    private Integer weight;// 排序权重，0 时候表示不按此排序

    @NotNull
    @Enumerated
    private PUBLISH_TYPE type;//发布类型，1表示文字，2表示图片，3表示视屏，4表示拍摄

    @NotNull
    private String title;//标题

    @ApiParam(hidden = true)
    private Date createTime;//创建时间

    private BigDecimal unlockPrice;//解锁需要金额
    @ApiParam(hidden = true)
    @Enumerated
    private STATUS status;//状态，正常/封禁/已删除

    @ApiParam(hidden = true)
    private Integer viewNum; // 浏览次数

    @ApiParam(hidden = true)
    private Integer commentNum;//留言数(不与数据库做交互)

    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publish_id")
    private List<PublishComment> comments; // 点赞人列表

    @ApiParam(hidden = true)
    private Integer likeNum;//点赞数(不与数据库做交互)

    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publish_id")
    private List<PublishLikedUser> likedUsers; // 点赞人列表


    private String labels;//标签（逗号隔开）

    @Column(length = Integer.MAX_VALUE)
    private String text;    // 文本内容

    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publish_id")
    private List<Picture> pictures;//图片(不与数据库做交互)

    @ApiParam(hidden = true)
    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private PublishVideo video;

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

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
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

    public List<PublishLikedUser> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<PublishLikedUser> likedUsers) {
        this.likedUsers = likedUsers;
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

    public PublishVideo getVideo() {
        return video;
    }

    public void setVideo(PublishVideo video) {
        this.video = video;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<PublishComment> getComments() {
        return comments;
    }

    public void setComments(List<PublishComment> comments) {
        this.comments = comments;
    }
}
