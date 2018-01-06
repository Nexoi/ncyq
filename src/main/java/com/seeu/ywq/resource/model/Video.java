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
}
