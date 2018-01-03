package com.seeu.ywq.user.dvo;

import com.seeu.ywq.user.model.User;

public class UserWithNickName extends User {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
