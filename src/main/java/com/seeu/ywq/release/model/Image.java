package com.seeu.ywq.release.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

/**
 * 图片信息，存在云端，用唯一ID进行查询
 */
@Entity
@Table(name = "image")
public class Image {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String imageUrl;
    @ApiParam(hidden = true)
    private Integer height;
    @ApiParam(hidden = true)
    private Integer width;
    // 缩略图
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbImage100pxUrl;
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbImage200pxUrl;
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbImage300pxUrl;
    @ApiParam(hidden = true)
    @Column(length = 400)
    private String thumbImage500pxUrl;
    @ApiParam(hidden = true)
    private Date createDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbImage100pxUrl() {
        return thumbImage100pxUrl;
    }

    public void setThumbImage100pxUrl(String thumbImage100pxUrl) {
        this.thumbImage100pxUrl = thumbImage100pxUrl;
    }

    public String getThumbImage200pxUrl() {
        return thumbImage200pxUrl;
    }

    public void setThumbImage200pxUrl(String thumbImage200pxUrl) {
        this.thumbImage200pxUrl = thumbImage200pxUrl;
    }

    public String getThumbImage300pxUrl() {
        return thumbImage300pxUrl;
    }

    public void setThumbImage300pxUrl(String thumbImage300pxUrl) {
        this.thumbImage300pxUrl = thumbImage300pxUrl;
    }

    public String getThumbImage500pxUrl() {
        return thumbImage500pxUrl;
    }

    public void setThumbImage500pxUrl(String thumbImage500pxUrl) {
        this.thumbImage500pxUrl = thumbImage500pxUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
