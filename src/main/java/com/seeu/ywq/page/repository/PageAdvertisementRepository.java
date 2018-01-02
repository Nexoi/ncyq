package com.seeu.ywq.page.repository;

import com.seeu.ywq.page.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageAdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAllByCategory(@Param("category") Advertisement.CATEGORY category);
}
