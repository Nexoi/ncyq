package com.seeu.ywq.gift.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ywq_reward_order")
public class RewardOrder {
    @Id
    @Column(length = 20)
    private String orderId;
    private Long uid;
    private Long herUid;
    private Long rewardResourceId;
    private Long diamonds;
    private Date createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getHerUid() {
        return herUid;
    }

    public void setHerUid(Long herUid) {
        this.herUid = herUid;
    }

    public Long getRewardResourceId() {
        return rewardResourceId;
    }

    public void setRewardResourceId(Long rewardResourceId) {
        this.rewardResourceId = rewardResourceId;
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
