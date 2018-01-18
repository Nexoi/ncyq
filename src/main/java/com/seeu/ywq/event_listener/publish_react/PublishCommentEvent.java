package com.seeu.ywq.event_listener.publish_react;

import org.springframework.context.ApplicationEvent;

public class PublishCommentEvent extends ApplicationEvent {
    private Long herUid;
    private Long uid;
    private String nickname;
    private String headIconUrl;
    private Long publishId;
    private String text;
    private String imgUrl;

    public PublishCommentEvent(Object source, Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String text, String imgUrl) {
        super(source);
        this.herUid = herUid;
        this.uid = uid;
        this.nickname = nickname;
        this.headIconUrl = headIconUrl;
        this.publishId = publishId;
        this.text = text;
        this.imgUrl = imgUrl;
    }

    public Long getHerUid() {
        return herUid;
    }

    public Long getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public Long getPublishId() {
        return publishId;
    }

    public String getText() {
        return text;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
