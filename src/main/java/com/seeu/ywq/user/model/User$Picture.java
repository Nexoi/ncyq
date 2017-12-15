package com.seeu.ywq.user.model;

import javax.persistence.*;

@Entity
@Table(name = "picture_with_user", indexes = {
        @Index(name = "ALI_PICTURE_USER_INDEX1", columnList = "picture_id"),
        @Index(name = "ALI_PICTURE_USER_INDEX2", columnList = "uid")
})
public class User$Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "picture_id")
    private Long pictureId;
    @Column(name = "uid")
    private Long uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
