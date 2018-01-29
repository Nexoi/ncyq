package com.seeu.ywq.admin.repository;

import com.seeu.ywq.admin.model.BindUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 7:21 PM
 * Describe:
 */

@PreAuthorize("hasRole('ADMIN')")
public interface BindUserRepository extends JpaRepository<BindUser, Long> {

    Page<BindUser> findAllByAdminUid(@Param("adminUid") Long uid, Pageable pageable);
}
