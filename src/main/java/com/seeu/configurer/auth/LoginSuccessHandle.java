package com.seeu.configurer.auth;

import com.seeu.core.Util4IP;
import com.seeu.ywq.release.service.UserPositionService;
import com.seeu.ywq.release.service.impl.UserPositionServiceImpl;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by neo on 10/10/2017.
 * <p>
 * 登录成功后返回登录前页面
 */
@Component("loginSuccessHandle")
public class LoginSuccessHandle implements AuthenticationSuccessHandler {

    @Autowired
    private UserPositionService userPositionService;
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private Util4IP util4IP;

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
        String longitudeStr = httpServletRequest.getParameter("longitude");
        String latitudeStr = httpServletRequest.getParameter("latitude");
        if (!authentication.isAuthenticated())
            return;
        Long uid = ((UserLogin) authentication.getPrincipal()).getUid();
        if (latitudeStr != null && longitudeStr != null) {
            BigDecimal longitude = BigDecimal.valueOf(Double.parseDouble(longitudeStr));
            BigDecimal latitude = BigDecimal.valueOf(Double.parseDouble(latitudeStr));
            userPositionService.updatePosition(uid, longitude, latitude);
        }
        String ip = util4IP.getIpAddress(httpServletRequest);
        userLoginLogService.updateLog(uid, ip, new Date());
    }
}
