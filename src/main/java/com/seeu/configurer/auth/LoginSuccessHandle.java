package com.seeu.configurer.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by neo on 10/10/2017.
 * <p>
 * 登录成功后返回登录前页面
 */
public class LoginSuccessHandle implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // TODO
//        Object url = httpServletRequest.getSession().getAttribute("loginSuccessURL");
//        String redirectUrl = url == null ? "/" : url.toString();
//        if (redirectUrl.contains("/signin")) {
//            redirectUrl = "/";
//        }
//        httpServletResponse.sendRedirect(redirectUrl);
        // 更新登陆信息
    }
}
