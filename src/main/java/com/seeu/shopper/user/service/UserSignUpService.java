package com.seeu.shopper.user.service;

import com.seeu.shopper.user.model.USER_STATUS;
import com.seeu.shopper.user.repository.UserLoginRepository;
import com.seeu.shopper.user.model.UserLogin;
import com.seeu.shopper.utils.EmailService;
import com.seeu.shopper.utils.MD5Service;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import com.seeu.shopper.utils.jwt.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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
        signup_active_waiting,  // 等待注册
        signup_active_success,  // 注册成功
        signup_active_failure,  // 注册激活失败
        signup_error_email_send,    // 发送失败
        signup_error_email,
        signup_error_name,
        signup_error_password,  // 密码长度／复杂度有误
        // 注销
        written_off_success,
        written_off_failure,
        // 异常
        sign_exception
    }

    @Resource
    UserLoginRepository userLoginRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtConstant jwtConstant;
    @Autowired
    MD5Service md5Service;

    /**
     * 发送邮件进行验证注册
     *
     * @param name
     * @param email
     * @param password     始末可以为空格，长度大于等于 6 即可【强制】
     * @param needChecking 是否需要验证邮箱
     */
    public SIGN_STATUS signUp(String name, String email, String password, boolean needChecking) {
        // 规整化字符串
        if (name == null) return SIGN_STATUS.signup_error_name;
        name = name.trim();
        if (email == null) return SIGN_STATUS.signup_error_email;
        email = email.trim();
        if (password == null || password.length() < 6) return SIGN_STATUS.signup_error_password;

        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(email);
        userLogin.setPassword(md5Service.encode(password));

        if (needChecking) {
            // 添加用户信息，状态为 -2【未激活用户】
            userLogin.setMemberStatus(USER_STATUS.UNACTIVED);
            userLoginRepository.save(userLogin);
            try {
                // send
                boolean sendSuccess = sendEmail(name, email);
                return sendSuccess ? SIGN_STATUS.signup_active_waiting : SIGN_STATUS.signup_error_email_send;
            } catch (Exception e) {
                return SIGN_STATUS.sign_exception;
            }
        } else {
            // 直接添加，状态为 1【正常用户】
            userLogin.setMemberStatus(USER_STATUS.OK);
            userLoginRepository.save(userLogin);
            return SIGN_STATUS.signup_success;
        }
    }

    /**
     * 激活用户账号
     *
     * @param token
     * @return
     */
    public SIGN_STATUS activeAccount(String token) {
        // 解析 token
        EmailRegistUserToken user = jwtUtil.parseToken(token);
        if (user != null) {
            UserLogin userLogin = userLoginRepository.findByEmail(user.getEmail());
            if (userLogin == null) {
                // 用户不存在，不能激活该用户
                return SIGN_STATUS.signup_active_failure;
            }
            if (userLogin.getMemberStatus() != USER_STATUS.UNACTIVED) {
                // 用户已经被激活，不需要重新激活，用户状态不会改变（可能并不是 1【正常用户】）
                return SIGN_STATUS.signup_active_success;
            }
            // 更新用户状态为 1【正常用户】
            userLogin.setMemberStatus(USER_STATUS.OK);
            userLoginRepository.save(userLogin);
            return SIGN_STATUS.signup_active_success;
        }
        // 默认注册失败，异常
        return SIGN_STATUS.sign_exception;
    }

    public void signUp(String phone) {
        // TODO regist by phone number
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

    /**
     * 如果发送成功则返回 true
     *
     * @param name
     * @param email
     * @return
     * @throws Exception, EmailException
     */
    private boolean sendEmail(String name, String email) throws EmailException, Exception {
        // 生成 token 发送至对方邮箱
        EmailRegistUserToken user = new EmailRegistUserToken();
        user.setEmail(email);
        String subject = jwtUtil.generalSubject(user);
        try {
            String token = jwtUtil.createJWT(jwtConstant.getJWT_ID(), subject, jwtConstant.getJWT_INTERVAL());
            String url = checkingURL + token;
            return emailService.send(email, title, getContent(name, url));
        } catch (AddressException e1) {
            throw new EmailException("address error");
        } catch (MessagingException e2) {
            throw new EmailException("messaging error");
        } catch (EmailException e3) {
            throw e3;
        } catch (Exception e4) {
            throw e4;
        }
    }


    private String title = "请激活您的账号";

    private String getContent(String name, String url) {
        String content = "" + url;
        return content;
    }

}
