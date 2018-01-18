package com.seeu.ywq.exception;

/**
 * 操作不支持
 * 比如：
 * 未点赞但进行取消点赞操作
 * 未关注但进行取消关注操作
 */
public class ActionNotSupportException extends Exception {
    public ActionNotSupportException(String message) {
        super(message);
    }
}
