package com.seeu.ywq.resource.repository;

import com.seeu.ywq.resource.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
