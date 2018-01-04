package com.seeu.ywq.globalconfig.service.impl;

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

    @Resource
    private GlobalConfigurerRepository repository;

    @Override
    public String findOne(String attrName) {
        GlobalConfigurer configurer = repository.findOne(attrName);
        return (configurer == null) ? null : configurer.getAttrValue();
    }

    @Override
    public void setUnlockWeChat(Long diamonds) {
        GlobalConfigurer configurer = repository.findOne(KEY_UNLOCK_WECHAT);
        if (configurer == null) {
            configurer = new GlobalConfigurer();
            configurer.setAttrKey(KEY_UNLOCK_WECHAT);
            configurer.setAttrValue("0");
        }
        configurer.setAttrKey(String.valueOf(diamonds));
        repository.save(configurer);
    }

    @Override
    public Long getUnlockWeChat() {
        String diamonds = findOne(KEY_UNLOCK_WECHAT);
        return diamonds == null ? 0L : Long.parseLong(diamonds);
    }

    @Override
    public void setBindUserShareDiamondsPercent(float percent) {
        if (percent < 0 || percent > 1)
            return;
        GlobalConfigurer configurer = repository.findOne(KEY_BIND_USER_DIAMOND_PERCENT);
        if (configurer == null) {
            configurer = new GlobalConfigurer();
            configurer.setAttrKey(KEY_BIND_USER_DIAMOND_PERCENT);
            configurer.setAttrValue("0.0");
        }
        configurer.setAttrKey(String.valueOf(percent));
        repository.save(configurer);
    }

    @Override
    public float getBindUserShareDiamondsPercent() {
        String percent = findOne(KEY_BIND_USER_DIAMOND_PERCENT);
        return percent == null ? 0.00f : Float.parseFloat(percent);
    }

    @Override
    public void setUserRewardDiamondsPercent(float percent) {
        if (percent < 0 || percent > 1)
            return;
        GlobalConfigurer configurer = repository.findOne(KEY_USER_DIAMOND_PERCENT);
        if (configurer == null) {
            configurer = new GlobalConfigurer();
            configurer.setAttrKey(KEY_USER_DIAMOND_PERCENT);
            configurer.setAttrValue("0.00");
        }
        configurer.setAttrKey(String.valueOf(percent));
        repository.save(configurer);
    }

    @Override
    public float getUserDiamondsPercent() {
        String percent = findOne(KEY_USER_DIAMOND_PERCENT);
        return percent == null ? 0.00f : Float.parseFloat(percent);
    }
}
