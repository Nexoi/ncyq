package com.seeu.configurer;


import com.seeu.ywq.userlogin.model.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                System.out.println("SESSION ID >> " + request.getRequestedSessionId());
                request.getSession().setAttribute("loginSuccessURL", request.getHeader("referer"));
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                response.setCharacterEncoding("UTF-8");
//                response.setHeader("Content-type", "application/json;charset=UTF-8");
                // 给页面添加是否登录状态信息
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal != null && !principal.equals("anonymousUser") && modelAndView != null) {
                    UserLogin authUser = (UserLogin) principal;
                    modelAndView.addObject("signed", authUser.getUsername()); // email
                }
            }
        }).addPathPatterns("/**").excludePathPatterns("/signin","/*.xml");
    }
}
