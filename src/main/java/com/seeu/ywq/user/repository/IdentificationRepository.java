package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRepository extends JpaRepository<Identification,Long> {
}
