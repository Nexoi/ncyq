package com.seeu.ywq.trend.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PicturesDTO {

    @NotNull
    private List<PictureDTO> pictures;

    public List<PictureDTO> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureDTO> pictures) {
        this.pictures = pictures;
    }
}
