package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.PublishLikedUser;
import com.seeu.ywq.release.model.PublishLikedUserPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublishLikedUserRepository extends JpaRepository<PublishLikedUser, PublishLikedUserPKeys> {
    void deleteAllByPublishId(@Param("publishId") Long publishId);

}
