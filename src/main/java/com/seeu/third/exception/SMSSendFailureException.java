package com.seeu.third.exception;

public class SMSSendFailureException extends Exception {

    public SMSSendFailureException(String phone, String message) {
        super("短信发送失败：【Phone】" + phone + "|【Message】" + message);
    }
}
