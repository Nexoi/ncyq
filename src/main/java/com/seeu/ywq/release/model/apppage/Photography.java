package com.seeu.ywq.release.model.apppage;

import com.seeu.ywq.release.model.Image;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户摄影作品（管理员摄影后上传，用户可下载）
 */
@Entity
@Table(name = "ywq_photography", indexes = {
        @Index(name = "photography_index1", columnList = "uid")
})
public class Photography {

    public enum DELETE_FLAG {
        show,
        delete
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    private Long uid;

    @ApiParam(hidden = true)
    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
}
