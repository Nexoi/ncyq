package com.seeu.ywq.release.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 照片墙，一个用户最多 5 张
 */
@Entity
@Table(name = "photo_wall", indexes = {
        @Index(name = "Photo_wall_index1", columnList = "uid")
})
public class PhotoWall {
    public enum PHOTO_WALL_DELETE_FLAG {
        show,
        delete
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @ApiParam(hidden = true)
    @NotNull
    private Long uid;//用户账号

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    private Image image;//图

    @ApiParam(hidden = true)
    private Date createTime;//创建时间

    @ApiParam(hidden = true)
    private Date deleteTime;//删除时间

    @ApiParam(hidden = true)
    private PHOTO_WALL_DELETE_FLAG deleteFlag;//删除标记

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

    public PHOTO_WALL_DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(PHOTO_WALL_DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
