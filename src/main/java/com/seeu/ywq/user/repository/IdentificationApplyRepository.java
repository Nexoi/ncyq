package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.IdentificationApplyPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface IdentificationApplyRepository extends JpaRepository<IdentificationApply, IdentificationApplyPKeys> {
    IdentificationApply findFirstByUidOrderByCreateTimeDesc(@Param("uid") Long uid);
}
