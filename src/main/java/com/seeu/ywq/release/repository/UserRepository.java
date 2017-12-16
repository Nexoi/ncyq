package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
