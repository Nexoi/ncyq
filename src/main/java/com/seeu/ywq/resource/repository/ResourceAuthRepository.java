package com.seeu.ywq.resource.repository;

import com.seeu.ywq.resource.model.ResourceAuth;
import com.seeu.ywq.resource.model.ResourceAuthPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ResourceAuthRepository extends JpaRepository<ResourceAuth, ResourceAuthPKeys> {
    Integer countAllByUidAndResourceIdAndOutTimeAfter(@Param("uid") Long uid, @Param("resourceId") Long resourceId, @Param("outTime") Date currentTime);
}
