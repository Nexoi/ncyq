package com.seeu.ywq.trend.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PublishCommentDTO {
    @Length(min = 1, max = 400, message = "评论不能为空，且字数不大于 400")
    @NotNull
    private String text;

    private Long fatherId; // 父评论ID

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }
}
