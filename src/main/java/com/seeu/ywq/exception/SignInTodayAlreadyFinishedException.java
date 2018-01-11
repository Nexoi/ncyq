package com.seeu.ywq.exception;

public class SignInTodayAlreadyFinishedException extends Exception {
    public SignInTodayAlreadyFinishedException(Long uid) {
        super("用户已经签过到了！[UID: " + uid + " ]");
    }
}
