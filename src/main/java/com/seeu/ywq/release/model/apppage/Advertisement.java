package com.seeu.ywq.release.model.apppage;

import com.seeu.ywq.release.model.Image;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_page_advertisement")
public class Advertisement {
    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Image image;
    @Column(length = 400)
    private String url;

    private Date updateTime;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
