package com.seeu.ywq.trend.dto;

import com.seeu.ywq.trend.model.Picture;

import javax.validation.constraints.NotNull;

public class PictureDTO {
    @NotNull
    private Picture.ALBUM_TYPE albumType;//相册类型(公开、私密)

    @NotNull
    private ImageDTO imageOpen;

    //    @NotNull
    private ImageDTO imageClose;

    public Picture.ALBUM_TYPE getAlbumType() {
        return albumType;
    }

    public void setAlbumType(Picture.ALBUM_TYPE albumType) {
        this.albumType = albumType;
    }

    public ImageDTO getImageOpen() {
        return imageOpen;
    }

    public void setImageOpen(ImageDTO imageOpen) {
        this.imageOpen = imageOpen;
    }

    public ImageDTO getImageClose() {
        return imageClose;
    }

    public void setImageClose(ImageDTO imageClose) {
        this.imageClose = imageClose;
    }
}
