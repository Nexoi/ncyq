package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
