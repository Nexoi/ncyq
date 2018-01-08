package com.seeu.ywq.pay.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单，即：钻石交易日志
 */
@Entity
@Table(name = "ywq_pay_order_log", indexes = {
        @Index(name = "ORDER_LOG_INDEX1", columnList = "uid"),
        @Index(name = "ORDER_LOG_INDEX2", columnList = "event")
})
@IdClass(OrderLogPKeys.class)
public class OrderLog {

    public enum EVENT {
        REWARD_EXPENSE(true),
        REWARD_RECEIVE(false),
        RECHARGE(false),
        WITHDRAW(true),
        UNLOCK_PUBLISH(true),
        RECEIVE_PUBLISH(false),
        UNLOCK_WECHAT(true),
        RECEIVE_WECHAT(false),
        UNLOCK_PHONE(true),
        RECEIVE_PHONE(false),
        BIND_SHARED_EXPENSE(true),
        BIND_SHARED_RECEIVE(false),
        DIAMOND_TO_COIN(true);

        boolean expense;

        EVENT(boolean expense) {
            this.expense = expense;
        }

        public boolean isExpense() {
            return true;
        }

        public boolean canShareBind() {
            switch (this) {
                case REWARD_EXPENSE:
                case UNLOCK_WECHAT:
                case UNLOCK_PHONE:
                case UNLOCK_PUBLISH:
                    return true;
                default:
                    return false;
            }
        }
    }

    public enum TYPE {
        IN,
        OUT
    }

    @Id
    @Column(length = 20)
    private String orderId; // 20180101095648123123  // yyyyMMddHHmmssS{随机数}

    @Id
    private Long uid;

    private EVENT event;

    private TYPE type;

    private Long diamonds;

    private Date createTime;        // 订单创建时间（创建即视作完成交易）

    public OrderLog() {
    }

    public OrderLog(String orderId, Long uid, EVENT event, TYPE type, Long diamonds, Date createTime) {
        this.orderId = orderId;
        this.uid = uid;
        this.event = event;
        this.type = type;
        this.diamonds = diamonds;
        this.createTime = createTime;
    }

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

    public EVENT getEvent() {
        return event;
    }

    public void setEvent(EVENT event) {
        this.event = event;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
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
