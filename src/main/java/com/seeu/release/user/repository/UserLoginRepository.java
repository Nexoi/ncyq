package com.seeu.release.user.repository;

import com.seeu.release.user.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    public UserLogin findByEmail(String email);
}