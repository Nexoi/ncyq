package com.seeu.ywq.trend.dvo;

import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.resource.model.Image;

import java.util.Date;

/**
 * 返回给前端的 model
 */
public class PublishPictureVO {
    private Long id;//主键

    private Long uid;

    private Picture.ALBUM_TYPE albumType;//相册类型(公开、私密)

    private Long publishId; //发布动态的 id

    private Image image;//

    private Date createTime;//创建时间

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

    public Picture.ALBUM_TYPE getAlbumType() {
        return albumType;
    }

    public void setAlbumType(Picture.ALBUM_TYPE albumType) {
        this.albumType = albumType;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
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
}
