package com.seeu.ywq.admin.service.impl;

import com.seeu.ywq.admin.model.BindUser;
import com.seeu.ywq.admin.repository.BindUserRepository;
import com.seeu.ywq.admin.service.BindUserService;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:22 PM
 * Describe:
 *
 * TODO
 */
@Service
public class BindUserServiceImpl implements BindUserService {
    @Resource
    private BindUserRepository repository;

    @Override
    public Page<BindUser> findAll(Long adminUid, Pageable pageable) {
        return repository.findAllByAdminUid(adminUid, pageable);
    }

    @Override
    public BindUser findOne(Long uid) {
        return repository.findOne(uid);
    }

    @Override
    public BindUser bind(Long adminUid, Long userUid) {
        return null;
    }

    @Override
    public UserLogin createUser(Long adminUid) {
        return null;
    }

    @Override
    public UserLogin update(Long adminUid, UserLogin user) {
        return null;
    }

    @Override
    public void deleteUser(Long adminUid, Long userUid) {

    }
}
