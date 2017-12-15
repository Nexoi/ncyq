package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.Publish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PublishRepository extends JpaRepository<Publish, Long> {
    Publish findByIdAndUid(@Param("id") Long id, @Param("uid") Long uid);

    Page findAllByUid(@Param("uid") Long uid, Pageable pageable);

    Page findAllByIdAndUid(@Param("id") Long id, @Param("uid") Long uid, Pageable pageable);
}
