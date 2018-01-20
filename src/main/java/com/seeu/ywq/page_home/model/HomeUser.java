package com.seeu.ywq.page_home.model;

import com.seeu.ywq.resource.model.Image;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 3:59 PM
 * Describe:
 * 首页人物
 */
@Entity
@Table(name = "ywq_page_home_users", indexes = {
        @Index(name = "home_users_index1", columnList = "label")
})
public class HomeUser {
    public enum LABEL {
        youwu,
        hotperson
    }

    public enum DELETE {
        show,
        delete
    }

    @Id
    private Long uid;
    private String coverImageUrl;
    @Column(name = "label")
    private LABEL label;
    @Enumerated
    private DELETE deleteFlag;
    private Date createTime;

    // 从其他地方注入进来
    @Transient
    private String nickname;  //
    @Transient
    private String headIconUrl;

    // 是否喜欢该用户
    @Transient
    private Boolean likeIt;
    //    private Long fansNum;   // 粉丝数
    //    private Long followNum; // 关注人数
    @Transient
    private Long likeNum;   // 点赞人数

    @Transient
    private List<Long> identifications;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public LABEL getLabel() {
        return label;
    }

    public void setLabel(LABEL label) {
        this.label = label;
    }

    public DELETE getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Boolean getLikeIt() {
        return likeIt;
    }

    public void setLikeIt(Boolean likeIt) {
        this.likeIt = likeIt;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public List<Long> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Long> identifications) {
        this.identifications = identifications;
    }
}
