package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
