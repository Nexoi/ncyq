package com.seeu.ywq.pay.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ywq_pay_order_recharge")
public class OrderRecharge {
    public enum PAY_METHOD {
        ALIPAY,
        WECHAT
    }

    public enum PAY_STATUS {
        wait_for_pay,
        paid_success,
        timeout_cancel,
        failure
    }

    @Id
    private String orderId;

    private Long uid;

    private PAY_METHOD payMethod;

    private BigDecimal price;

    private Long diamonds;

    private Date createTime;        // 订单创建时间

    private Date paymentTime;       // 付款时间（因为不可退款，所以付款成功即为交易结束）

    private PAY_STATUS payStatus;

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

    public PAY_METHOD getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PAY_METHOD payMethod) {
        this.payMethod = payMethod;
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

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public PAY_STATUS getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PAY_STATUS payStatus) {
        this.payStatus = payStatus;
    }
}
