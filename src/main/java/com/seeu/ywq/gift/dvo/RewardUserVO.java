package com.seeu.ywq.gift.dvo;

import com.seeu.ywq.user.dvo.SimpleUserVO;

import java.util.Date;

public class RewardUserVO {
    private SimpleUserVO user;
    private Long diamonds;
    private Date createTime;

    public SimpleUserVO getUser() {
        return user;
    }

    public void setUser(SimpleUserVO user) {
        this.user = user;
    }

    public Long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Long diamonds) {
        this.diamonds = diamonds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
