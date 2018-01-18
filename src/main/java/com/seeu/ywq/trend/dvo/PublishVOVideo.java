package com.seeu.ywq.trend.dvo;

import java.math.BigDecimal;

public class PublishVOVideo extends PublishVO {
    private Long unlockPrice;//解锁需要金额
    private PublishVideoVO video;

    public Long getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(Long unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public PublishVideoVO getVideo() {
        return video;
    }

    public void setVideo(PublishVideoVO video) {
        this.video = video;
    }

}
