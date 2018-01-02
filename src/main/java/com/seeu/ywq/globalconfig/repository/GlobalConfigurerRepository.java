package com.seeu.ywq.globalconfig.repository;

import com.seeu.ywq.globalconfig.model.GlobalConfigurer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigurerRepository extends JpaRepository<GlobalConfigurer, String> {
}
