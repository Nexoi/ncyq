package com.seeu.ywq.user.dto;

import com.seeu.ywq.user.model.Picture;

import java.math.BigDecimal;
import java.util.List;

public class PublishPictureVO extends PublishVO {
    private BigDecimal unlockPrice;//解锁需要金额
    private PictureVO coverPictureUrl;//动态广场中动态封面图片(不与数据库做交互)
    private List<PictureVO> pictures;//图片(不与数据库做交互)

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PictureVO getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public void setCoverPictureUrl(PictureVO coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }

    public List<PictureVO> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureVO> pictures) {
        this.pictures = pictures;
    }
}
