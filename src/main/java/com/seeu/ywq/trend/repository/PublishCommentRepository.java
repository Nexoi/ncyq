package com.seeu.ywq.trend.repository;

import com.seeu.ywq.trend.model.PublishComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PublishCommentRepository extends JpaRepository<PublishComment, Long> {
    void deleteAllByPublishId(@Param("publishId") Long publishId);

    Page findAllByPublishIdAndFatherIdIsNull(@Param("publishId") Long publishId, Pageable pageable);
}
