package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Publish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PublishRepository extends JpaRepository<Publish, Long> {
    Publish findByIdAndUid(@Param("id") Long id, @Param("uid") Long uid);

    Page findAllByUid(@Param("uid") Long uid, Pageable pageable);

    Page findAllByIdAndUid(@Param("id") Long id, @Param("uid") Long uid, Pageable pageable);

    // 浏览一次
    @Transactional
    @Modifying
    @Query("update Publish p set p.viewNum = p.viewNum + 1 where p.id = :id")
    void viewItOnce(@Param("id") Long id);

    // 点赞一次
    @Transactional
    @Modifying
    @Query("update Publish p set p.likeNum = p.likeNum + 1 where p.id = :id")
    void likeItOnce(@Param("id") Long id);

    // 取消点赞一次
    @Transactional
    @Modifying
    @Query("update Publish p set p.likeNum = p.likeNum - 1 where p.id = :id")
    void dislikeItOnce(@Param("id") Long id);

    // 评论一次
    @Transactional
    @Modifying
    @Query("update Publish p set p.commentNum = p.commentNum + 1 where p.id = :id")
    void commentItOnce(@Param("id") Long id);

    // 取消评论一次
    @Transactional
    @Modifying
    @Query("update Publish p set p.commentNum = p.commentNum - 1 where p.id = :id")
    void disCommentItOnce(@Param("id") Long id);
}
