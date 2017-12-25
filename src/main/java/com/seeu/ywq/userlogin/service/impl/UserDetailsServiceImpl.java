package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserReactService userReactService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userReactService.findByPhone(s);
    }
}
