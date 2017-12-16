package com.seeu.ywq.release.model;

import java.io.Serializable;

public class Publish$UserKeys implements Serializable {
    private Long publishId;
    private Long uid;

    public Publish$UserKeys() {
    }

    public Publish$UserKeys(Long publishId, Long uid) {
        this.publishId = publishId;
        this.uid = uid;
    }


    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
