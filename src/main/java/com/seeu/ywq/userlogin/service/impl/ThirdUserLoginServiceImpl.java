package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import com.seeu.ywq.userlogin.repository.ThirdUserLoginRepository;
import com.seeu.ywq.userlogin.service.ThirdUserLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ThirdUserLoginServiceImpl implements ThirdUserLoginService {

    @Resource
    private ThirdUserLoginRepository repository;

    @Override
    public ThirdUserLogin findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public boolean exists(String name) {
        return repository.exists(name);
    }

    @Override
    public ThirdUserLogin save(ThirdUserLogin thirdUserLogin) {
        return repository.save(thirdUserLogin);
    }

    @Override
    public ThirdUserLogin bindUid(String name, Long uid) {
        ThirdUserLogin ul = repository.findByName(name);
        if (ul == null || uid == null) return null;
        ul.setYwqUid(uid);
        return repository.save(ul);
    }

}
