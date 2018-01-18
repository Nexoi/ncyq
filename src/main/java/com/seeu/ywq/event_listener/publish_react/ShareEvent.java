package com.seeu.ywq.event_listener.publish_react;

import org.springframework.context.ApplicationEvent;

public class ShareEvent extends ApplicationEvent {
    private Long uid;
    private String url;
    private Long publishId;

    public ShareEvent(Object source, Long uid, String url, Long publishId) {
        super(source);
        this.uid = uid;
        this.url = url;
        this.publishId = publishId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }
}
