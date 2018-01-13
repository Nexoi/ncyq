package com.seeu.ywq.ywqactivity.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ywq_activity")
public class Activity {
    public enum DELETE_FLAG {
        show,
        delete
    }

    @Id
    private Long id;
    private String title;
    private String subTitle;
    @Column(length = 400)
    private String url;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Enumerated
    private DELETE_FLAG deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
