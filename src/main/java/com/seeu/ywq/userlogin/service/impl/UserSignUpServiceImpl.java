package com.seeu.ywq.userlogin.service.impl;

import com.seeu.system.sms.service.ISmsSV;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.repository.PayBalanceRepository;
import com.seeu.ywq.release.model.User;
import com.seeu.ywq.release.repository.UserRepository;
import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserAuthRole;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserAuthRoleRepository;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import com.seeu.ywq.userlogin.service.UserSignUpService;
import com.seeu.ywq.utils.MD5Service;
import com.seeu.ywq.utils.jwt.JwtConstant;
import com.seeu.ywq.utils.jwt.JwtUtil;
import com.seeu.ywq.utils.jwt.PhoneCodeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserSignUpServiceImpl implements UserSignUpService {
    @Resource
    UserLoginRepository userLoginRepository;
    @Resource
    UserAuthRoleRepository userAuthRoleRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    PayBalanceRepository payBalanceRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;
    @Autowired
    private ISmsSV iSmsSV;


    public UserSignUpService.SignUpPhoneResult sendPhoneMessage(String phone) {
        // 此处生成 6 位验证码
        String code = null;
        // 开始发送，这里使用的网易云信，貌似可以自动随机数验证码
        UserSignUpService.SIGN_PHONE_SEND status = null;
        try {
            code = iSmsSV.sendSMS(phone);
            status = code != null ? UserSignUpService.SIGN_PHONE_SEND.success : UserSignUpService.SIGN_PHONE_SEND.failure;
        } catch (Exception e) {
            code = null;
            status = UserSignUpService.SIGN_PHONE_SEND.failure;
        }
        //...
        UserSignUpService.SignUpPhoneResult result = new UserSignUpService.SignUpPhoneResult();
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
    public UserSignUpService.SIGN_STATUS signUp(String name, String phone, String password, String code, String signCheck) {
        // 验证验证码
        if (signCheck == null || signCheck.trim().length() == 0)
            return UserSignUpService.SIGN_STATUS.signup_error_sign_check;
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signCheck);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                || !phoneCodeToken.getPhone().equals(phone) // 电话号码不一致
                || !phoneCodeToken.getCode().equals(code)   // 验证码不一致
                ) {
            return UserSignUpService.SIGN_STATUS.signup_error_sign_check;
        }

        // 规整化字符串
        if (name == null) return UserSignUpService.SIGN_STATUS.signup_error_name;
        name = name.trim();
        if (phone == null) return UserSignUpService.SIGN_STATUS.signup_error_phone;
        phone = phone.trim();
        if (password == null || password.length() < 6) return UserSignUpService.SIGN_STATUS.signup_error_password;

        UserLogin userLogin = new UserLogin();
        userLogin.setNickname(name);
        userLogin.setPhone(phone);
        userLogin.setPassword(md5Service.encode(password));
        userLogin.setLikeNum(0l);

        // 直接添加，状态为 1【正常用户】
        userLogin.setMemberStatus(USER_STATUS.OK);
        // 添加权限 //
        List<UserAuthRole> roles = new ArrayList<>();
        UserAuthRole userAuthRole = userAuthRoleRepository.findByName("ROLE_USER");
        roles.add(userAuthRole);
        userLogin.setRoles(roles);
        UserLogin savedUserLogin = userLoginRepository.save(userLogin);
        // 添加用户基本信息 //
        User user = new User();
        user.setUid(savedUserLogin.getUid());
        user.setPhone(phone);
        userRepository.saveAndFlush(user);
        // 添加用户余额系统 //
        Balance balance = new Balance();
        balance.setUid(savedUserLogin.getUid());
        balance.setBalance(0l);
        balance.setUpdateTime(new Date());
        payBalanceRepository.save(balance);
        return UserSignUpService.SIGN_STATUS.signup_success;
    }

    /**
     * 注销用户，状态置为 -1
     *
     * @param uid
     * @return
     */
    public UserSignUpService.SIGN_STATUS writtenOff(Long uid) {
        if (!userLoginRepository.exists(uid))
            return UserSignUpService.SIGN_STATUS.written_off_failure;
        UserLogin user = new UserLogin();
        user.setUid(uid);
        user.setMemberStatus(USER_STATUS.DISTORY);
        userLoginRepository.save(user);
        return UserSignUpService.SIGN_STATUS.written_off_success;
    }


    private String genCode() {
        return Integer.toString(100000 + (int) Math.random() * 899999);
    }
}
