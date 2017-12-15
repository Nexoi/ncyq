package com.seeu.ywq.user.dto;

import java.math.BigDecimal;

public class PublishVideoVO extends PublishVO{
    private BigDecimal unlockPrice;//解锁需要金额
    private String coverVideoUrl;
    private String videoUrls;//

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public String getCoverVideoUrl() {
        return coverVideoUrl;
    }

    public void setCoverVideoUrl(String coverVideoUrl) {
        this.coverVideoUrl = coverVideoUrl;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }
}
