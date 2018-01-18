package com.seeu.ywq.utils;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;

public interface ThirdPartTokenService {
    public interface Processor {
        public void process(boolean isValidated, String username, String nickname, String headIconUrl);
    }

    void validatedInfo(ThirdUserLogin.TYPE type, String username, String token, Processor processor);
}
