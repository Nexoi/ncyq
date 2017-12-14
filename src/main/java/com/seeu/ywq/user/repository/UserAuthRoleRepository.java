package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.UserAuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserAuthRoleRepository extends JpaRepository<UserAuthRole, Integer> {
    UserAuthRole findByName(@Param("name") String name);
}
