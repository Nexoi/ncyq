package com.seeu.ywq.admin.service;

import com.seeu.ywq.admin.model.BindUser;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:22 PM
 * Describe:
 */

public interface BindUserService {

    Page<BindUser> findAll(Long adminUid, Pageable pageable);

    List<Long> findAll(Long adminUid);

    BindUser findOne(Long uid);

    BindUser bind(Long adminUid, Long userUid);

    UserLogin update(Long adminUid, UserLogin user);

    void deleteUser(Long adminUid, Long userUid); // 删除并解绑

    boolean canOperateUser(Long adminUid, Long userUid);
}
