package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.TokenPersistent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.EnableAsync;

public interface TokenPersistentRepository extends JpaRepository<TokenPersistent, String> {
    void deleteByUsername(@Param("username") String username);
}
