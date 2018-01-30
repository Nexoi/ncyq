package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.IdentificationApplyPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IdentificationApplyRepository extends JpaRepository<IdentificationApply, IdentificationApplyPKeys> {
    IdentificationApply findFirstByUidOrderByCreateTimeDesc(@Param("uid") Long uid);

    Page<IdentificationApply> findAllByOrderByCreateTimeDesc(Pageable pageable);

    Page<IdentificationApply> findAllByUidOrderByCreateTimeDesc(@Param("uid") Long uid, Pageable pageable);

    Page<IdentificationApply> findAllByStatusInOrderByCreateTimeDesc(@Param("statuses") Collection<IdentificationApply.STATUS> statuses, Pageable pageable);
}
