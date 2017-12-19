package com.seeu.ywq.release.model.apppage;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_page_configurer", indexes = {
        @Index(name = "page_config_index1", columnList = "category"),
        @Index(name = "page_config_index2", columnList = "order_id")
})
public class PageConfig {

    public enum CATEGORY {
        HOMEPAGE_Advertisements,
        HOMEPAGE_NewHotsPerson,
        HOMEPAGE_NewActors,
        YouWuPage_New,
        HotsPerson_New,
        Video_Advertisements
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "category")
    @Enumerated
    private CATEGORY category;
    private Long srcId;
    @Column(name = "order_id")
    private Long orderId;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    public Long getSrcId() {
        return srcId;
    }

    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
