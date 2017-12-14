package com.seeu.ywq.bean;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 相册表
 *
 * @author Scary
 */
@Entity
@Table(name = "album", indexes = {
        @Index(name = "album_index1", columnList = "uid")
})
public class Album implements Serializable {

    public enum ALBUM_TYPE {
        photewal,
        open,
        close
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @NotNull
    private Long uid;//用户账号

    @Enumerated
    private ALBUM_TYPE albumType;//相册类型(1照片墙、2公开、3私密)

    private String mainUrl;//封面展示图地址

    private Date createTime;//创建时间

    private Date deleteTime;//删除时间

    private String deleteFlag;//删除标记


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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }


}
