package com.seeu.ywq.admin.service;

import com.seeu.ywq.admin.model.BindUser;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:22 PM
 * Describe:
 */

public interface BindUserService {

    Page<BindUser> findAll(Long adminUid, Pageable pageable);

    BindUser findOne(Long uid);

    BindUser bind(Long adminUid, Long userUid);

    UserLogin createUser(Long adminUid); // 会返回一个默认用户的基础信息，之后自行修改

    UserLogin update(Long adminUid, UserLogin user);

    void deleteUser(Long adminUid, Long userUid); // 删除并解绑
}
