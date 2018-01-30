package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.UserIdentification;
import com.seeu.ywq.user.model.UserIdentificationPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User$IdentificationRepository extends JpaRepository<UserIdentification, UserIdentificationPKeys> {
    List<UserIdentification> findAllByUid(@Param("uid") Long uid);

    UserIdentification findAllByUidAAndIdentificationId(@Param("uid") Long uid, @Param("identificationId") Long identificationId);


    List<UserIdentification> findAllByUidAndStatus(@Param("uid") Long uid, @Param("status") IdentificationApply.STATUS status);

    List<UserIdentification> findAllByUidAndStatusNot(@Param("uid") Long uid, @Param("status") IdentificationApply.STATUS status);
}
