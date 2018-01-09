package com.seeu.ywq.userlogin.service.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.repository.UserInfoRepository;
import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserAuthRole;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserAuthRoleRepository;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.userlogin.service.UserSignUpService;
import com.seeu.ywq.utils.MD5Service;
import com.seeu.ywq.utils.jwt.JwtConstant;
import com.seeu.ywq.utils.jwt.JwtUtil;
import com.seeu.ywq.utils.jwt.PhoneCodeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserSignUpServiceImpl implements UserSignUpService {
    @Resource
    UserReactService userReactService;
    @Resource
    UserAuthRoleRepository userAuthRoleRepository;
    @Resource
    UserInfoRepository userInfoRepository;
    @Resource
    BalanceService balanceService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;
    @Autowired
    private SMSService smsService;
    @Value("${ywq.sms.regist_sendcode}")
    private String message;
    @Value("${ywq.headicon}")
    private String headIcon;

    public UserSignUpService.SignUpPhoneResult sendPhoneMessage(String phone) {
        // 此处生成 6 位验证码
//        String code = String.valueOf(100000 + new Random().nextInt(899999));
        String code = "123456";
        UserSignUpService.SIGN_PHONE_SEND status = null;
        try {
//            code = iSmsSV.sendSMS(phone);
            smsService.send(phone, message.replace("%code%", code));
            status = UserSignUpService.SIGN_PHONE_SEND.success;
        } catch (SMSSendFailureException e) {
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
     * 得到手机验证码进行验证注册
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
        userLogin.setHeadIconUrl(headIcon);

        // 直接添加，状态为 1【正常用户】
        userLogin.setMemberStatus(USER_STATUS.OK);
        // 添加权限 //
        List<UserAuthRole> roles = new ArrayList<>();
        UserAuthRole userAuthRole = userAuthRoleRepository.findByName("ROLE_USER");
        roles.add(userAuthRole);
        userLogin.setRoles(roles);
        UserLogin savedUserLogin = userReactService.save(userLogin);
        // 更新昵称
        savedUserLogin.setNickname("user_" + savedUserLogin.getUid());
        savedUserLogin = userReactService.save(savedUserLogin);
        // 添加用户基本信息 //
        User user = new User();
        user.setUid(savedUserLogin.getUid());
        user.setPhone(phone);
        user.setPublishNum(0L);
        user.setFansNum(0L);
        user.setFollowNum(0L);
        user.setLikeNum(0L);
        user.setBirthDay(new Date()); // 默认今天
        userInfoRepository.saveAndFlush(user);
        // 初始化用户余额系统 //
        balanceService.initAccount(savedUserLogin.getUid(), null);
        return UserSignUpService.SIGN_STATUS.signup_success;
    }

    /**
     * 注销用户，状态置为 -1
     *
     * @param uid
     * @return
     */
    public UserSignUpService.SIGN_STATUS writtenOff(Long uid) {
        if (!userReactService.exists(uid))
            return UserSignUpService.SIGN_STATUS.written_off_failure;
        UserLogin user = new UserLogin();
        user.setUid(uid);
        user.setMemberStatus(USER_STATUS.DISTORY);
        userReactService.save(user);
        return UserSignUpService.SIGN_STATUS.written_off_success;
    }


    private String genCode() {
        return Integer.toString(100000 + (int) Math.random() * 899999);
    }
}
