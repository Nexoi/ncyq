package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.User$Identification;
import com.seeu.ywq.release.model.User$IdentificationPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User$IdentificationRepository extends JpaRepository<User$Identification, User$IdentificationPKeys> {
    List<User$Identification> findAllByUidAndStatus(@Param("uid") Long uid, @Param("status") User$Identification.STATUS status);

    List<User$Identification> findAllByUidAndStatusNot(@Param("uid") Long uid, @Param("status") User$Identification.STATUS status);
}
