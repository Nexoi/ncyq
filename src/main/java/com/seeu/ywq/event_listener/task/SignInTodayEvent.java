package com.seeu.ywq.event_listener.task;

import org.springframework.context.ApplicationEvent;

public class SignInTodayEvent extends ApplicationEvent {
    private Long uid;

    public SignInTodayEvent(Object source, Long uid) {
        super(source);
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
