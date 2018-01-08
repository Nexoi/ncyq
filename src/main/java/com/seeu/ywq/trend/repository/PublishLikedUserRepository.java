package com.seeu.ywq.trend.repository;

import com.seeu.ywq.trend.model.PublishLikedUser;
import com.seeu.ywq.trend.model.PublishLikedUserPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PublishLikedUserRepository extends JpaRepository<PublishLikedUser, PublishLikedUserPKeys> {
    @Transactional
    @Modifying
    void removeAllByPublishId(@Param("publishId") Long publishId);

    Page findAllByPublishId(@Param("publishId") Long publishId, Pageable pageable);

}
