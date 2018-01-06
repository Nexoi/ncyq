package com.seeu.ywq.userlogin.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(Long uid) {
        super("用户【UID：" + uid + "】不存在");
    }
}
