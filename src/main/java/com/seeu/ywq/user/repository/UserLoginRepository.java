package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    UserLogin findByPhone(@Param("phone") String phone);
}