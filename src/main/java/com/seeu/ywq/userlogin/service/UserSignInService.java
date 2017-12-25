package com.seeu.ywq.userlogin.service;

import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by neo on 25/11/2017.
 * <p>
 * 用户登录
 * <p>
 * 提供：
 * 1. 邮箱登录
 * 2. 手机登录（ing）
 */
@Service
public class UserSignInService {
    public enum SIGN_STATUS {
        // 登录
        signin_success,
        signin_error_password,
        signin_error_no_such_user,
        // 异常
        sign_exception
    }

    @Autowired
    private UserReactService userReactService;

    /**
     * 邮箱登录
     *
     * @param email
     * @param password 请使用密码的 hashcode 进行登录
     * @return
     */
    public SIGN_STATUS signIn(String email, String password) {
        if (email == null || password == null)
            return SIGN_STATUS.signin_error_no_such_user;
        email = email.trim();
        UserLogin user = userReactService.findByPhone(email);
        if (user == null)
            return SIGN_STATUS.signin_error_no_such_user;
        if (!user.getPassword().equals(password))
            return SIGN_STATUS.signin_error_password;
        if (user.getMemberStatus() != USER_STATUS.UNACTIVED) // 未激活用户不可登录
            return SIGN_STATUS.signin_success;
        return SIGN_STATUS.sign_exception;
    }
}
