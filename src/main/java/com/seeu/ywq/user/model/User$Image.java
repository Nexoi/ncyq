package com.seeu.ywq.user.model;

import javax.persistence.*;

@Entity
@Table(name = "image_with_user", indexes = {
        @Index(name = "ALI_IMAGE_USER_INDEX1", columnList = "image_id"),
        @Index(name = "ALI_IMAGE_USER_INDEX2", columnList = "uid")
})
public class User$Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "uid")
    private Long uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
