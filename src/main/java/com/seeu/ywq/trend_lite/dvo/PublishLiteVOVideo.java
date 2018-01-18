package com.seeu.ywq.trend_lite.dvo;

import com.seeu.ywq.trend.dvo.PublishVideoVO;

public class PublishLiteVOVideo extends PublishLiteVO {
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
