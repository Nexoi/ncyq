package com.seeu.ywq.user.service;

import com.seeu.system.sms.service.ISmsSV;
import com.seeu.ywq.user.model.USER_STATUS;
import com.seeu.ywq.user.repository.UserLoginRepository;
import com.seeu.ywq.user.model.UserLogin;
import com.seeu.ywq.utils.MD5Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.seeu.ywq.utils.jwt.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by neo on 25/11/2017.
 * <p>
 * 用户注册
 * <p>
 * <p>
 * 提供：
 * 1. 发送邮件进行验证注册
 * 2. 手机注册
 * 3. 直接注册
 * <p>
 * <p>
 * 密码会被自动注册为 hashcode，登录时请前台传入用户的 hash 值进行验证
 * 邮件模版在 getContent 方法体内，验证地址在此类修改
 */
@Service
public class UserSignUpService {

    private static final String checkingURL = "http://demo.com/active?token=";

    public enum SIGN_STATUS {
        // 注册
        signup_success,
        signup_failure,
        // 字段错误
        signup_error_phone,
        signup_error_name,
        signup_error_password,  // 密码长度／复杂度有误
        signup_error_sign_check, // 验证码加密信息
        // 注销
        written_off_success,
        written_off_failure,
        // 异常
        sign_exception
    }

    public enum SIGN_PHONE_SEND {
        success,
        failure
    }

    @Resource
    UserLoginRepository userLoginRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;
    @Autowired
    private ISmsSV iSmsSV;


    public SignUpPhoneResult sendPhoneMessage(String phone) {
        // 此处生成 6 位验证码
        String code = null;
        // 开始发送，这里使用的网易云信，貌似可以自动随机数验证码
        SIGN_PHONE_SEND status = null;
        try {
            code = iSmsSV.sendSMS(phone);
            status = code != null ? SIGN_PHONE_SEND.success : SIGN_PHONE_SEND.failure;
        } catch (Exception e) {
            code = null;
            status = SIGN_PHONE_SEND.failure;
        }
        //...
        SignUpPhoneResult result = new SignUpPhoneResult();
        result.setCode(code);
        result.setStatus(status);
        return result;
    }

    public String genSignCheckToken(String phone, String code) {
        PhoneCodeToken codeToken = new PhoneCodeToken();
        codeToken.setPhone(phone);
        codeToken.setCode(code);
        String subject = jwtUtil.generalSubject(codeToken);
        try {
            String token = jwtUtil.createJWT(jwtConstant.getJWT_ID(), subject, jwtConstant.getJWT_INTERVAL());
            return token;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 发送手机验证码进行验证注册
     *
     * @param name
     * @param phone
     * @param password  始末可以为空格，长度大于等于 6 即可【强制】
     * @param signCheck 验证手机号码和验证码是否匹配
     */
    public SIGN_STATUS signUp(String name, String phone, String password, String signCheck) {
        // 验证验证码
        if (signCheck == null || signCheck.trim().length() == 0)
            return SIGN_STATUS.signup_error_sign_check;
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signCheck);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                || !phoneCodeToken.getPhone().equals(phone) // 电话号码不一致
                ) {
            return SIGN_STATUS.signup_error_sign_check;
        }

        // 规整化字符串
        if (name == null) return SIGN_STATUS.signup_error_name;
        name = name.trim();
        if (phone == null) return SIGN_STATUS.signup_error_phone;
        phone = phone.trim();
        if (password == null || password.length() < 6) return SIGN_STATUS.signup_error_password;

        UserLogin userLogin = new UserLogin();
        userLogin.setUsername(name);
        userLogin.setPhone(phone);
        userLogin.setPassword(md5Service.encode(password));

        // 直接添加，状态为 1【正常用户】
        userLogin.setMemberStatus(USER_STATUS.OK);
        userLoginRepository.save(userLogin);
        return SIGN_STATUS.signup_success;
    }

    /**
     * 注销用户，状态置为 -1
     *
     * @param uid
     * @return
     */
    public SIGN_STATUS writtenOff(Long uid) {
        if (!userLoginRepository.exists(uid))
            return SIGN_STATUS.written_off_failure;
        UserLogin user = new UserLogin();
        user.setUid(uid);
        user.setMemberStatus(USER_STATUS.DISTORY);
        userLoginRepository.save(user);
        return SIGN_STATUS.written_off_success;
    }


    private boolean sendPhone(String phone) {
        // 发送手机验证码

        return true;
    }

    private String genCode() {
        return Integer.toString(100000 + (int) Math.random() * 899999);
    }

    public class SignUpPhoneResult {
        private SIGN_PHONE_SEND status;
        private String code;

        public SIGN_PHONE_SEND getStatus() {
            return status;
        }

        public void setStatus(SIGN_PHONE_SEND status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
