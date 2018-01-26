package com.seeu.ywq.event_listener.yellowpicture;

import org.springframework.context.ApplicationEvent;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 5:50 PM
 * Describe:
 */

public class YellowEvent extends ApplicationEvent {
    private Long uid;
    private Long publishId;
    private Long pictureId;
    private Double score;

    public YellowEvent(Object source, Long uid, Long publishId, Long pictureId, Double score) {
        super(source);
        this.uid = uid;
        this.publishId = publishId;
        this.pictureId = pictureId;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }
}
