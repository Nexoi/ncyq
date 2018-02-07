package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.model.UserLoginAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by suneo.
 * User: neo
 * Date: 08/02/2018
 * Time: 1:58 AM
 * Describe:
 */

public interface UserLoginAccessRepository extends JpaRepository<UserLoginAccess, Long> {

    // admin //

    Page<UserLogin> findAllByUidIn(@Param("uid") Collection<Long> uids, Pageable pageable);
}
