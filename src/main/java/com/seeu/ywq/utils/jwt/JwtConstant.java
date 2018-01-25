package com.seeu.ywq.utils.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by neo on 02/07/2017.
 */
@Component
@PropertySource("classpath:application.properties")
public class JwtConstant {
    @Value("${com.seeu.jwt.secret}")
    private String JWT_SECRET;
    @Value("${com.seeu.jwt.id}")
    private String JWT_ID;
    @Value("${com.seeu.jwt.interval}")
    private int JWT_INTERVAL;
    @Value("${com.seeu.jwt.token_interval}")
    private int JWT_TOKEN_INTERVAL;

    public String getJWT_SECRET() {
        return JWT_SECRET;
    }

    public void setJWT_SECRET(String JWT_SECRET) {
        this.JWT_SECRET = JWT_SECRET;
    }

    public String getJWT_ID() {
        return JWT_ID;
    }

    public void setJWT_ID(String JWT_ID) {
        this.JWT_ID = JWT_ID;
    }

    public int getJWT_INTERVAL() {
        return JWT_INTERVAL;
    }

    public void setJWT_INTERVAL(int JWT_INTERVAL) {
        this.JWT_INTERVAL = JWT_INTERVAL;
    }

    public int getJWT_TOKEN_INTERVAL() {
        return JWT_TOKEN_INTERVAL;
    }

    public void setJWT_TOKEN_INTERVAL(int JWT_TOKEN_INTERVAL) {
        this.JWT_TOKEN_INTERVAL = JWT_TOKEN_INTERVAL;
    }
}
