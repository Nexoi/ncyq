package com.seeu.ywq.page.model;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_page_video")
public class HomePageVideo {
    public enum CATEGORY {
        hd,
        vr
    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private CATEGORY category;
    private String title;
    private Long uid;
    private Long viewNum;

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "cover_image_id", referencedColumnName = "id")
    private Image coverImage;

    @OneToOne(targetEntity = Video.class)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video video;

    private DELETE_FLAG deleteFlag;

    private Date createTime;

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
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
}
