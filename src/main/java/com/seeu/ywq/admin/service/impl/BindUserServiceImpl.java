package com.seeu.ywq.admin.service.impl;

import com.seeu.ywq.admin.model.BindUser;
import com.seeu.ywq.admin.repository.BindUserRepository;
import com.seeu.ywq.admin.service.BindUserService;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:22 PM
 * Describe:
 * <p>
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
    public List<Long> findAll(Long adminUid) {
        List<BindUser> users = repository.findAll();
        if (users.size() == 0) return new ArrayList<>();
        List<Long> uids = new ArrayList<>();
        for (BindUser user : users) {
            if (user != null)
                uids.add(user.getNickUid());
        }
        return uids;
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
    public UserLogin update(Long adminUid, UserLogin user) {
        return null;
    }

    @Override
    public void deleteUser(Long adminUid, Long userUid) {

    }

    @Override
    public boolean canOperateUser(Long adminUid, Long userUid) {
        BindUser user = repository.findOne(userUid);
        return user.getAdminUid().equals(adminUid);
    }
}
