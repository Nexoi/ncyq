package com.seeu.ywq.page_advertisement.repository;

import com.seeu.ywq.page_advertisement.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAllByCategory(@Param("category") Advertisement.CATEGORY category);
}
