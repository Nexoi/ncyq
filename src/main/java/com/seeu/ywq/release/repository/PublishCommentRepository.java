package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.PublishComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PublishCommentRepository extends JpaRepository<PublishComment,Long> {
    void deleteAllByPublishId(@Param("publishId") Long publishId);
}
