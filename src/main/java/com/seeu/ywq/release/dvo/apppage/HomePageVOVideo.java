package com.seeu.ywq.release.dvo.apppage;

import com.seeu.ywq.release.model.PublishVideo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 这其实是在主页的动态内容。。。
 */
public class HomePageVOVideo {

    private Long id;//主键

    private Long uid;//用户账号

    private String title;//标题

    private Date createTime;//创建时间

    private Integer viewNum; // 浏览次数

    private Integer commentNum;//留言数(不与数据库做交互)

    private Integer likeNum;//点赞数(不与数据库做交互)

//    private String text;    // 文本内容

    private BigDecimal unlockPrice;//解锁需要金额
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

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PublishVideo getVideo() {
        return video;
    }

    public void setVideo(PublishVideo video) {
        this.video = video;
    }
}
