package com.seeu.ywq.release.dvo;

import com.seeu.ywq.release.model.PublishVideo;

import java.math.BigDecimal;

public class PublishVOVideo extends PublishVO {
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
