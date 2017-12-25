package com.seeu.ywq.release.dvo.apppage;

import com.seeu.ywq.release.dvo.PublishPictureVO;
import com.seeu.ywq.release.model.apppage.PublishLite;

import java.math.BigDecimal;
import java.util.List;

public class PublishLiteVOPicture extends PublishLiteVO{
    private BigDecimal unlockPrice;//解锁需要金额
    private PublishPictureVO coverPictureUrl;//动态广场中动态封面图片(不与数据库做交互)
    private List<PublishPictureVO> pictures;//图片(不与数据库做交互)

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
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
