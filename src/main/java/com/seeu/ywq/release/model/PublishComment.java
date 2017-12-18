package com.seeu.ywq.release.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 评论列表
 */
@Entity
@Table(name = "ywq_publish_comments", indexes = {
        @Index(name = "COMMENT_INDEX1", columnList = "father_id"),
        @Index(name = "COMMENT_INDEX2", columnList = "publish_id"),
        @Index(name = "COMMENT_INDEX3", columnList = "uid")
})
public class PublishComment {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(name = "publish_id")
    private Long publishId;
    @ApiParam(hidden = true)
    private Long uid;
    @ApiParam(hidden = true)
    private String username;
    @ApiParam(hidden = true)
    private String headIconUrl;
    @ApiParam(hidden = true)
    private Date commentDate;
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String text;

    @ApiParam(hidden = true)
    @Column(name = "father_id")
    private Long fatherId; // 父评论ID
    @ApiParam(hidden = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id")
    private List<PublishComment> childComments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
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

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public List<PublishComment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<PublishComment> childComments) {
        this.childComments = childComments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
