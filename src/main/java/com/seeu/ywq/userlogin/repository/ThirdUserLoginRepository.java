package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ThirdUserLoginRepository extends JpaRepository<ThirdUserLogin, String> {
    ThirdUserLogin findByName(@Param("name") String name);
}
