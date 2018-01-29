package com.seeu.ywq.pay.model;

import com.seeu.ywq.pay.dto.AliPayNotifyDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 11:21 AM
 * Describe:
 */
@Entity
@Table(name = "ywq_pay_trade")
public class TradeModel {
    public enum TRADE_STATUS {   // 交易状态（四个可选值）
        WAIT_BUYER_PAY,     // 交易创建，等待买家付款
        TRADE_CLOSED,       // 未付款交易超时关闭，或支付完成后全额退款
        TRADE_SUCCESS,      // 交易支付成功
        TRADE_FINISHED      // 交易结束，不可退款
    }

    public enum PAYMENT {
        ALIPAY,
        WECHAT
    }

    @Id
    @Column(name = "order_id", length = 20)
    private String orderId;

    private PAYMENT payment;

    private Long uid;

    private String subject;

    private String body;

    @Enumerated
    private TRADE_STATUS status; // 交易状态

    @Column(precision = 20, scale = 2)
    private BigDecimal price;

    private Long diamonds; // 特别字段，表示充值了多少钻石，部分功能可能使用

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TRADE_STATUS getStatus() {
        return status;
    }

    public void setStatus(TRADE_STATUS status) {
        this.status = status;
    }

    public PAYMENT getPayment() {
        return payment;
    }

    public void setPayment(PAYMENT payment) {
        this.payment = payment;
    }
}
