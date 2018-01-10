package com.seeu.ywq.globalconfig.service;

import com.seeu.ywq.exception.ActionNotSupportException;

import java.math.BigDecimal;

public interface GlobalConfigurerService {
    String findOne(String attrName);
    // 将所有的配置都单独列出来

    void setUnlockWeChat(Long diamonds) throws ActionNotSupportException;

    Long getUnlockWeChat();

    // 用户上家分成比例
    void setBindUserShareDiamondsPercent(float percent) throws ActionNotSupportException;

    float getBindUserShareDiamondsPercent();

    // 用户自己分成比例
    void setUserDiamondsPercent(float percent) throws ActionNotSupportException;

    float getUserDiamondsPercent();

    // 钻石-金币汇率（一颗钻石相当于多少金币）
    void setDiamondToCoinsRatio(Integer ratio) throws ActionNotSupportException;

    Integer getDiamondToCoinsRatio();

    // 钻石转换为金币
    Long getCoinsRatioToDiamonds(Long diamonds);

    // RMB-钻石汇率
    void setRMBToDiamondsRatio(Integer ratio) throws ActionNotSupportException;

    Integer getRMBToDiamondsRatio();

    // RMB转换为钻石
    Long getDiamondsRatioToRMB(BigDecimal price);
}
