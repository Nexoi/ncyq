package com.seeu.ywq.page.dvo;

import com.seeu.ywq.trend.model.PublishVideo;

import java.math.BigDecimal;

public class PublishLiteVOVideo extends PublishLiteVO {
    private BigDecimal unlockPrice;//解锁需要金额
    private PublishVideo video;

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PublishVideo getVideo() {
        return video;
    }

    public void setVideo(PublishVideo video) {
        this.video = video;
    }
}
