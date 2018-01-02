package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.User$Identification;
import com.seeu.ywq.user.model.User$IdentificationPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User$IdentificationRepository extends JpaRepository<User$Identification, User$IdentificationPKeys> {
    List<User$Identification> findAllByUid(@Param("uid") Long uid);

    List<User$Identification> findAllByUidAndStatus(@Param("uid") Long uid, @Param("status") User$Identification.STATUS status);

    List<User$Identification> findAllByUidAndStatusNot(@Param("uid") Long uid, @Param("status") User$Identification.STATUS status);
}
