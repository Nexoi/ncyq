package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_tag")
public class Tag {
    public enum DELETE_FLAG{
        show,
        delete
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(length = 20)
    private String tagName;

    @ApiParam(hidden = true)
    private Date createTime;

    @ApiParam(hidden = true)
    private DELETE_FLAG deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
