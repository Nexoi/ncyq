package com.seeu.ywq.globalconfig.service.impl;

import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.globalconfig.model.GlobalConfigurer;
import com.seeu.ywq.globalconfig.repository.GlobalConfigurerRepository;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GlobalConfigurerServiceImpl implements GlobalConfigurerService {

    private static final String KEY_UNLOCK_WECHAT = "unlock.wechat"; // 解锁微信消耗钻石数
    private static final String KEY_BIND_USER_DIAMOND_PERCENT = "binduser.diamond.percent"; // 用户上家分钱比例
    private static final String KEY_USER_DIAMOND_PERCENT = "user.diamond.percent"; // 用户分钱比例
    private static final String KEY_DIAMOND_2_COIN_RATIO = "diamond.2.coin.ratio"; // 钻石/金币汇率（ '1:20' 由 '20' 表示）

    private Long unlockWeChat;
    private Float bindUserShareDiamondsPercent;
    private Float userDiamondsPercent;
    private Integer diamondToCoinRatio;

    @Resource
    private GlobalConfigurerRepository repository;

    @Override
    public String findOne(String attrName) {
        GlobalConfigurer configurer = repository.findOne(attrName);
        return (configurer == null) ? null : configurer.getAttrValue();
    }

    @Override
    public void setUnlockWeChat(Long diamonds) throws ActionNotSupportException {
        if (diamonds < 0)
            throw new ActionNotSupportException("设定值不能为负数");
        setValue(KEY_UNLOCK_WECHAT, String.valueOf(diamonds));
        unlockWeChat = diamonds;
    }

    @Override
    public Long getUnlockWeChat() {
        if (unlockWeChat != null)
            return unlockWeChat;
        String diamonds = findOne(KEY_UNLOCK_WECHAT);
        unlockWeChat = diamonds == null ? 0L : Long.parseLong(diamonds);
        return unlockWeChat;
    }

    @Override
    public void setBindUserShareDiamondsPercent(float percent) throws ActionNotSupportException {
        if (percent < 0 || percent > 1)
            throw new ActionNotSupportException("比例设定必须在范围 0 - 1 之间");
        setValue(KEY_BIND_USER_DIAMOND_PERCENT, String.valueOf(percent));
        bindUserShareDiamondsPercent = percent;
    }

    @Override
    public float getBindUserShareDiamondsPercent() {
        if (bindUserShareDiamondsPercent != null)
            return bindUserShareDiamondsPercent;
        String percent = findOne(KEY_BIND_USER_DIAMOND_PERCENT);
        bindUserShareDiamondsPercent = percent == null ? 0.00f : Float.parseFloat(percent);
        return bindUserShareDiamondsPercent;
    }

    @Override
    public void setUserDiamondsPercent(float percent) throws ActionNotSupportException {
        if (percent < 0 || percent > 1)
            throw new ActionNotSupportException("比例设定必须在范围 0 - 1 之间");
        setValue(KEY_USER_DIAMOND_PERCENT, String.valueOf(percent));
        userDiamondsPercent = percent;
    }

    @Override
    public float getUserDiamondsPercent() {
        if (userDiamondsPercent != null)
            return userDiamondsPercent;
        String percent = findOne(KEY_USER_DIAMOND_PERCENT);
        userDiamondsPercent = percent == null ? 0.00f : Float.parseFloat(percent);
        return userDiamondsPercent;
    }

    @Override
    public void setDiamondToCoinRatio(Integer ratio) throws ActionNotSupportException {
        if (ratio == null || ratio <= 0)
            throw new ActionNotSupportException("设定值不能为负数或零");
        setValue(KEY_DIAMOND_2_COIN_RATIO, String.valueOf(ratio));
        diamondToCoinRatio = ratio;
    }

    @Override
    public Integer getDiamondToCoinRatio() {
        if (diamondToCoinRatio != null)
            return diamondToCoinRatio;
        String coin = findOne(KEY_DIAMOND_2_COIN_RATIO);
        diamondToCoinRatio = coin == null ? 0 : Integer.parseInt(coin);
        return diamondToCoinRatio;
    }

    @Override
    public Long getCoinsRatioToDiamonds(Long diamonds) {
        if (diamonds == null || diamonds == 0) return 0L;
        Integer ratio = getDiamondToCoinRatio();
        if (ratio == null) return 0L;
        return ratio * diamonds;
    }


    private void setValue(String key, String value) {
        GlobalConfigurer configurer = repository.findOne(key);
        if (configurer == null) {
            configurer = new GlobalConfigurer();
            configurer.setAttrKey(key);
            configurer.setAttrValue("0");
        }
        configurer.setAttrKey(String.valueOf(value));
        repository.save(configurer);
    }
}
