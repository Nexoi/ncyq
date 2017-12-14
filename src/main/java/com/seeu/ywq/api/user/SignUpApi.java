package com.seeu.ywq.api.user;

import com.seeu.ywq.user.repository.UserLoginRepository;
import com.seeu.ywq.user.service.UserSignUpService;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SignUpApi {
    @Resource
    private UserLoginRepository userLoginRepository;
    @Autowired
    private UserSignUpService userSignUpService;


    @PostMapping("/signup/sendcode/{phone}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "验证码发送成功"),
            @ApiResponse(code = 400, message = "验证码发送失败")
    })
    public ResponseEntity sendPhone(@PathVariable("phone") String phone, HttpServletResponse response) {
        UserSignUpService.SignUpPhoneResult result = userSignUpService.sendPhoneMessage(phone);
        if (result.getStatus() != null && result.getStatus().equals(UserSignUpService.SIGN_PHONE_SEND.success)) {
            // 写入 cookie
            String signCheckToken = userSignUpService.genSignCheckToken(phone, result.getCode());
            Cookie cookie = new Cookie("signCheck", signCheckToken);
            cookie.setMaxAge(10 * 60 * 1000); // ms
            response.addCookie(cookie);
            return ResponseEntity.ok().body("验证码发送成功");
        }
        return ResponseEntity.badRequest().body("验证码发送失败");
    }


    @PostMapping("/signup")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功"),
            @ApiResponse(code = 4001, message = "注册失败，验证码错误"),
            @ApiResponse(code = 4002, message = "注册失败，手机号码有误"),
            @ApiResponse(code = 4003, message = "注册失败，昵称非法，不能为空"),
            @ApiResponse(code = 4004, message = "注册失败，密码需大于 6 位"),
            @ApiResponse(code = 400, message = "未知异常，请联系管理员"),
            @ApiResponse(code = 500, message = "注册失败，服务器异常，请稍后再试"),
    })
    public ResponseEntity signUp(@RequestParam String username,
                                 @RequestParam String phone,
                                 @RequestParam String password,
                                 @ApiParam(name = "注册码校验签名，存在 cookie 中，不需要手动传入")
                                 @CookieValue(required = false) String signCheck) {
        // 检查手机号码是否被注册
        if (userLoginRepository.findByPhone(phone) != null) {
            return ResponseEntity.badRequest().body("该手机号码已被注册");
        }
        // start sign up
        UserSignUpService.SIGN_STATUS status = userSignUpService.signUp(username, phone, password, signCheck);
        switch (status) {
            case signup_success:
                return ResponseEntity.status(200).body("注册成功");
            case signup_error_sign_check:
                return ResponseEntity.status(4001).body("注册失败，验证码错误");
            case signup_error_phone:
                return ResponseEntity.status(4002).body("注册失败，手机号码有误");
            case signup_error_name:
                return ResponseEntity.status(4003).body("注册失败，昵称非法，不能为空");
            case signup_error_password:
                return ResponseEntity.status(4004).body("注册失败，密码需大于 6 位");
            case sign_exception:
                return ResponseEntity.status(500).body("注册失败，服务器异常，请稍后再试");
            default:
                return ResponseEntity.badRequest().body("未知异常，请联系管理员");
        }
    }
}
