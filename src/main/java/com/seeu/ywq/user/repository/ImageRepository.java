package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
