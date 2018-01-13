package com.seeu.ywq.page.dvo;

import com.seeu.ywq.trend.dvo.PublishPictureVO;

import java.math.BigDecimal;
import java.util.List;

public class PublishLiteVOPicture extends PublishLiteVO{
    private Long unlockPrice;//解锁需要金额
    private PublishPictureVO coverPictureUrl;//动态广场中动态封面图片(不与数据库做交互)
    private List<PublishPictureVO> pictures;//图片(不与数据库做交互)

    public Long getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(Long unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PublishPictureVO getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public void setCoverPictureUrl(PublishPictureVO coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }

    public List<PublishPictureVO> getPictures() {
        return pictures;
    }

    public void setPictures(List<PublishPictureVO> pictures) {
        this.pictures = pictures;
    }
}
