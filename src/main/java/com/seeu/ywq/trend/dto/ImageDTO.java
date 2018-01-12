package com.seeu.ywq.trend.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 图片信息，存在云端，用唯一ID进行查询
 */
public class ImageDTO {

    @NotNull
    private String imageUrl;
    @NotNull
    private Integer height;
    @NotNull
    private Integer width;
    // 缩略图
    @NotNull
    private String thumbImage100pxUrl;
    @NotNull
    private String thumbImage200pxUrl;
    @NotNull
    private String thumbImage300pxUrl;
    @NotNull
    private String thumbImage500pxUrl;

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
