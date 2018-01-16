package com.seeu.ywq.resource.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;

@Entity
@Table(name = "ywq_video")
public class Video {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(name = "coverUrl")
    @Column(length = 1024)
    private String coverUrl;

    @ApiParam(name = "srcUrl")
    @Column(length = 1024)
    private String srcUrl;

    private Integer width;

    private Integer height;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
