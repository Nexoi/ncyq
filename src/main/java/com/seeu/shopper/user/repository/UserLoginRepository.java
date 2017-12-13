package com.seeu.shopper.user.repository;

import com.seeu.shopper.user.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    public UserLogin findByEmail(String email);
}