package com.seeu.ywq.exception;

import com.seeu.ywq.trend.model.Publish;

public class PublishTYPENotAllowedException extends Exception {
    public PublishTYPENotAllowedException(Publish.PUBLISH_TYPE type) {
        super("动态类型 [ " + type.name() + " ] 不支持该操作");
    }
}
