package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.User$Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface User$ImageRepository extends JpaRepository<User$Image, Long> {
    Integer countAllByUidAndImageId(@Param("uid") Long uid, @Param("imageId") Long imageId);
}
