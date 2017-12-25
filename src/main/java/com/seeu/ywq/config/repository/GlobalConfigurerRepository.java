package com.seeu.ywq.config.repository;

import com.seeu.ywq.config.model.GlobalConfigurer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigurerRepository extends JpaRepository<GlobalConfigurer, String> {
}
