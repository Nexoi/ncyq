package com.seeu.third.exception;

public class IMTokenGetException extends Exception {
    public IMTokenGetException(Long uid, String message) {
        super("用户【ID：" + uid + "】获取 Token 失败！Message：" + message);
    }
}
