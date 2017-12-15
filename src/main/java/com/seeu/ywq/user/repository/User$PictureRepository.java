package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.User$Image;
import com.seeu.ywq.user.model.User$Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface User$PictureRepository extends JpaRepository<User$Picture, Long> {
    Integer countAllByUidAndPictureId(@Param("uid") Long uid, @Param("pictureId") Long pictureId);
}
