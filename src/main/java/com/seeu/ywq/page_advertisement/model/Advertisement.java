package com.seeu.ywq.page_advertisement.model;

import com.seeu.ywq.resource.model.Image;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_page_advertisement")
public class Advertisement {
    public enum CATEGORY {
        HomePage,
        VideoPage,
        PublishPage
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated
    private CATEGORY category;

    private Integer orderId;

    @OneToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @Column(length = 400)
    private String url;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }
}
