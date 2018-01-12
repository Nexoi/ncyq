package com.seeu.ywq.page.dvo;

import com.seeu.ywq.trend.dvo.PublishVideoVO;
import com.seeu.ywq.trend.model.PublishVideo;

import java.math.BigDecimal;

public class PublishLiteVOVideo extends PublishLiteVO {
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
