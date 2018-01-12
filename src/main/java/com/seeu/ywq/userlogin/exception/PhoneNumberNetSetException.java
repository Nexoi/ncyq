package com.seeu.ywq.userlogin.exception;

public class PhoneNumberNetSetException extends Exception {
    public PhoneNumberNetSetException(Long uid) {
        super("用户 [UID: " + uid + " ] 手机号码未设定！");
    }
}
