package com.seeu.ywq.release.dvo;

import com.seeu.ywq.release.model.Publish;

import java.util.Date;
import java.util.List;

public class PublishVO {

    private Long id;//主键

    private Long uid;//用户账号

    private Publish.PUBLISH_TYPE type;//发布类型，1表示文字，2表示图片，3表示视屏，4表示拍摄

    private String title;//标题

    private Date createTime;//创建时间

    private Integer viewNum; // 浏览次数

    private Integer commentNum;//留言数(不与数据库做交互)

    private List<PublishCommentVO> comments; // 点赞人列表

    private Integer likeNum;//点赞数(不与数据库做交互)

    private List<PublishLikedUserVO> likedUsers; // 点赞人列表

    private List<String> labels;//标签（逗号隔开）

    private String text;    // 文本内容


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

    public Publish.PUBLISH_TYPE getType() {
        return type;
    }

    public void setType(Publish.PUBLISH_TYPE type) {
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

    public List<PublishLikedUserVO> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<PublishLikedUserVO> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<PublishCommentVO> getComments() {
        return comments;
    }

    public void setComments(List<PublishCommentVO> comments) {
        this.comments = comments;
    }
}
