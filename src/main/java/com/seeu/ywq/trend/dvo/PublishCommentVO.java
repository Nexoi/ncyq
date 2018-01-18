package com.seeu.ywq.trend.dvo;

import java.util.Date;
import java.util.List;

/**
 * 评论列表VO
 */
public class PublishCommentVO {
    private Long id;
    private Long uid;
    private String username;
    private String headIconUrl;
    private Date commentDate;
    private String text;
    private List<PublishCommentVO> childComments;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }


    public List<PublishCommentVO> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<PublishCommentVO> childComments) {
        this.childComments = childComments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
