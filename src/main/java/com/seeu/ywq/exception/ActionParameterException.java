package com.seeu.ywq.exception;

public class ActionParameterException extends Exception {
    public ActionParameterException(String parameter) {
        super("传入参数错误：" + parameter);
    }
}
