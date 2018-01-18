package com.seeu.ywq.utils.impl;

import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.utils.AppAuthFlushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppAuthFlushServiceImpl implements AppAuthFlushService {

    @Autowired
    private UserReactService userReactService;

    @Override
    public void flush(Long uid) {
        UserLogin ul = userReactService.findOne(uid);
        if (ul == null)
            return;
        // flush
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(ul, ul.getPassword(), ul.getAuthorities());
        context.setAuthentication(auth); //重新设置上下文中存储的用户权限
    }
}
