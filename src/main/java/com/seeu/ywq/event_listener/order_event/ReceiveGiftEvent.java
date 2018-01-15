package com.seeu.ywq.event_listener.order_event;

import org.springframework.context.ApplicationEvent;

public class ReceiveGiftEvent extends ApplicationEvent {
    private Long uid;
    private Long hisUid;
    private String hisNickname;
    private Long rewardResourceId;
    private String rewardResourceName;
    private Integer amount;
    private Long diamonds;
    private Long transactionalDiamonds; // 真实交易价格
    private String orderId;

    public ReceiveGiftEvent(Object source, Long uid, Long hisUid, String hisNickname, Long rewardResourceId, String rewardResourceName, Integer amount, Long diamonds, Long transactionalDiamonds, String orderId) {
        super(source);
        this.uid = uid;
        this.hisUid = hisUid;
        this.hisNickname = hisNickname;
        this.rewardResourceId = rewardResourceId;
        this.rewardResourceName = rewardResourceName;
        this.amount = amount;
        this.diamonds = diamonds;
        this.transactionalDiamonds = transactionalDiamonds;
        this.orderId = orderId;
    }

    public String getHisNickname() {
        return hisNickname;
    }

    public void setHisNickname(String hisNickname) {
        this.hisNickname = hisNickname;
    }

    public Long getRewardResourceId() {
        return rewardResourceId;
    }

    public void setRewardResourceId(Long rewardResourceId) {
        this.rewardResourceId = rewardResourceId;
    }

    public String getRewardResourceName() {
        return rewardResourceName;
    }

    public void setRewardResourceName(String rewardResourceName) {
        this.rewardResourceName = rewardResourceName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getHisUid() {
        return hisUid;
    }

    public void setHisUid(Long hisUid) {
        this.hisUid = hisUid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Long diamonds) {
        this.diamonds = diamonds;
    }

    public Long getTransactionalDiamonds() {
        return transactionalDiamonds;
    }

    public void setTransactionalDiamonds(Long transactionalDiamonds) {
        this.transactionalDiamonds = transactionalDiamonds;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
