package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.TokenPersistent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TokenPersistentRepository extends JpaRepository<TokenPersistent, String> {
    void deleteByUsername(@Param("username") String username);
}
