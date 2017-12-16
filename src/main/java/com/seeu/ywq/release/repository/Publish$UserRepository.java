package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Publish$User;
import com.seeu.ywq.release.model.Publish$UserKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface Publish$UserRepository extends JpaRepository<Publish$User, Publish$UserKeys> {
    Integer countAllByUidAndPublishId(@Param("uid") Long uid, @Param("publishId") Long publishId);
}
