package com.seeu.ywq.uservip.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * VIP 种类配置表
 */
@Entity
@Table(name = "ywq_user_vip_table")
public class VIPTable {

    @Id
    @NotNull
    private Long day;
    @NotNull
    private String title;
    @NotNull
    @Column(precision = 12, scale = 2)
    private BigDecimal price;
    @NotNull
    private Long diamonds;

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
