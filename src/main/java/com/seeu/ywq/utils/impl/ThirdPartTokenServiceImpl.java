package com.seeu.ywq.utils.impl;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import com.seeu.ywq.utils.ThirdPartTokenService;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartTokenServiceImpl implements ThirdPartTokenService {
    @Override
    public void validatedInfo(ThirdUserLogin.TYPE type, String username, String token, Processor processor) {

    }
}
