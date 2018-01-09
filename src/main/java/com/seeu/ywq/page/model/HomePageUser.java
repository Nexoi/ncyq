package com.seeu.ywq.page.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(HomePageUserPKeys.class)
@Table(name = "ywq_page_person", indexes = {
        @Index(name = "page_person_index1", columnList = "category"),
        @Index(name = "page_person_index2", columnList = "order_id")
})
public class HomePageUser {

    @Id
    @Column(name = "category")
    private Integer category;
    @Id
    @Column(name = "uid")
    private Long uid;
    @Column(name = "order_id")
    private Integer orderId;
    private Date createTime;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
