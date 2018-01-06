package com.seeu.ywq.trend.dvo;

import java.math.BigDecimal;

public class PublishVOVideo extends PublishVO {
    private BigDecimal unlockPrice;//解锁需要金额
    private PublishVideoVO video;

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PublishVideoVO getVideo() {
        return video;
    }

    public void setVideo(PublishVideoVO video) {
        this.video = video;
    }

}
