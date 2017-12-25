package com.seeu.ywq.config.service;

public interface GlobalConfigurerService {
    String findOne(String attrName);
    // 将所有的配置都单独列出来

    void setUnlockWeChat(Long diamonds);

    Long getUnlockWeChat();

    void setBindUserShareDiamondsPercent(float percent);

    float getBindUserShareDiamondsPercent();
}
