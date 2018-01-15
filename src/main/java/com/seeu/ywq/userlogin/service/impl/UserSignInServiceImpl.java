package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.userlogin.service.UserSignInService;
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
public class UserSignInServiceImpl {

    @Autowired
    private UserReactService userReactService;

    /**
     * 邮箱登录
     *
     * @param email
     * @param password 请使用密码的 hashcode 进行登录
     * @return
     */
    public UserSignInService.SIGN_STATUS signIn(String email, String password) {
        if (email == null || password == null)
            return UserSignInService.SIGN_STATUS.signin_error_no_such_user;
        email = email.trim();
        UserLogin user = userReactService.findByPhone(email);
        if (user == null)
            return UserSignInService.SIGN_STATUS.signin_error_no_such_user;
        if (!user.getPassword().equals(password))
            return UserSignInService.SIGN_STATUS.signin_error_password;
        if (user.getMemberStatus() != USER_STATUS.UNACTIVED) // 未激活用户不可登录
            return UserSignInService.SIGN_STATUS.signin_success;
        return UserSignInService.SIGN_STATUS.sign_exception;
    }
}
