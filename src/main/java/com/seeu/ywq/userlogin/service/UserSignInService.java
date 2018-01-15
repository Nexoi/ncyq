package com.seeu.ywq.userlogin.service;

public interface UserSignInService {
    public enum SIGN_STATUS {
        // 登录
        signin_success,
        signin_error_password,
        signin_error_no_such_user,
        // 异常
        sign_exception
    }

    SIGN_STATUS signIn(String email, String password);


}
