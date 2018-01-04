package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    UserLogin findByPhone(@Param("phone") String phone);

}