package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 相册，即照片，分为两种：私密、公开
 */
@Entity
@Table(name = "picture", indexes = {
        @Index(name = "PICTURE_INDEX1", columnList = "uid"),
        @Index(name = "PICTURE_INDEX2", columnList = "publish_id")
})
public class Picture {
    public enum ALBUM_TYPE {
        open,
        close
    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @ApiParam(hidden = true)
    @NotNull
    private Long uid;

    @ApiParam(hidden = true)
    @Enumerated
    private ALBUM_TYPE albumType;//相册类型(公开、私密)

    @ApiParam(hidden = true)
    @Column(name = "publish_id")
    private Long publishId; //发布动态的 id

    @ApiParam(hidden = true)
    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_open_id",referencedColumnName = "id")
    private Image imageOpen;

    @ApiParam(hidden = true)
    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_close_id",referencedColumnName = "id")
    private Image imageClose;

    @ApiParam(hidden = true)
    private Date createTime;//创建时间

    @ApiParam(hidden = true)
    private Date deleteTime;//删除时间

    @ApiParam(hidden = true)
    private DELETE_FLAG deleteFlag;//删除标记

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

    public ALBUM_TYPE getAlbumType() {
        return albumType;
    }

    public void setAlbumType(ALBUM_TYPE albumType) {
        this.albumType = albumType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Image getImageOpen() {
        return imageOpen;
    }

    public void setImageOpen(Image imageOpen) {
        this.imageOpen = imageOpen;
    }

    public Image getImageClose() {
        return imageClose;
    }

    public void setImageClose(Image imageClose) {
        this.imageClose = imageClose;
    }
}
