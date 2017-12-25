package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.TokenPersistent;
import com.seeu.ywq.userlogin.repository.TokenPersistentRepository;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TokenPersistentServiceImpl implements PersistentTokenRepository {
    @Resource
    private TokenPersistentRepository tokenPersistentRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        TokenPersistent persistent = new TokenPersistent();
        persistent.setToken(persistentRememberMeToken.getTokenValue());
        persistent.setSeries(persistentRememberMeToken.getSeries());
        persistent.setUsername(persistentRememberMeToken.getUsername());
        persistent.setLastUsed(persistentRememberMeToken.getDate());
        tokenPersistentRepository.save(persistent);
    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        TokenPersistent persistent = tokenPersistentRepository.findOne(seriesId);
        persistent.setToken(tokenValue);
        persistent.setLastUsed(lastUsed);
        tokenPersistentRepository.save(persistent);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        TokenPersistent persistent = tokenPersistentRepository.findOne(seriesId);
        if (persistent == null) return null;
        PersistentRememberMeToken rememberMeToken = new PersistentRememberMeToken(
                persistent.getUsername(),
                persistent.getSeries(),
                persistent.getToken(),
                persistent.getLastUsed()
        );
        return rememberMeToken;
    }

    @Override
    public void removeUserTokens(String username) {
        tokenPersistentRepository.deleteByUsername(username);
    }
}
