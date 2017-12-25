package com.seeu.ywq.pay.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_pay_balance", indexes = {
        @Index(name = "PAY_BALANCE_INDEX1", columnList = "bind_uid")
})
public class Balance {
    @Id
    private Long uid;

    @Column(name = "bind_uid")
    private Long bindUid;

    @Column(name = "balance")
    private Long balance;

    private Date updateTime = new Date();

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getBindUid() {
        return bindUid;
    }

    public void setBindUid(Long bindUid) {
        this.bindUid = bindUid;
    }
}
