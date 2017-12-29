package com.seeu.ywq.event_listener.publish_react;

import org.springframework.context.ApplicationEvent;

/**
 * 点赞事件
 */
public class ClickLikeEvent extends ApplicationEvent {
    private Long herUid;
    private Long uid;
    private String nickname;
    private String headIconUrl;
    private Long publishId;
    private String imgUrl;


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

    public String getImgUrl() {
        return imgUrl;
    }

    public Long getPublishId() {
        return publishId;
    }

    public ClickLikeEvent(Object source, Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String imgUrl) {
        super(source);
        // set
        this.herUid = herUid;
        this.uid = uid;
        this.nickname = nickname;
        this.headIconUrl = headIconUrl;
        this.imgUrl = imgUrl;
        this.publishId = publishId;
    }

}
