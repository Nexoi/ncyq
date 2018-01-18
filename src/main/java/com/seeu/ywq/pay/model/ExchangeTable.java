package com.seeu.ywq.pay.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ywq_pay_exchange_table")
public class ExchangeTable {
    public enum TYPE {
        DIAMOND2COIN,
        RMB2DIAMOND
    }

    @Id
    private Long id;
    @Enumerated
    private TYPE type;
    @Column(name = "price_from")
    private BigDecimal from;
    @Column(name = "price_to")
    private BigDecimal to;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
