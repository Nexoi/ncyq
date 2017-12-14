package com.seeu.ywq.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "publish_open_image")
public class PublishOpenImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @Column(name = "publish_id")
    private String publishId;//发布表id

    private String imageUrl;//图片地址

    private String thumbImageUrl;//缩略图片地址

    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
