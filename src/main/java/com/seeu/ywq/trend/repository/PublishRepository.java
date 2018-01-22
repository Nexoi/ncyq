package com.seeu.ywq.trend.repository;

import com.seeu.ywq.trend.model.Publish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PublishRepository extends JpaRepository<Publish, Long> {

    Publish findByIdAndStatus(@Param("id") Long id, @Param("status") Publish.STATUS status);

    Publish findByIdAndUidAndStatus(@Param("id") Long id, @Param("uid") Long uid, @Param("status") Publish.STATUS status);

    Page findAllByUidAndStatus(@Param("uid") Long uid, @Param("status") Publish.STATUS status, Pageable pageable);

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


    // admin //

    Page<Publish> findAllByStatus(@Param("status") Publish.STATUS status, Pageable pageable);


    Page<Publish> findAllByStatusAndId(@Param("status") Publish.STATUS status, @Param("id") Long id, Pageable pageable);

    Page<Publish> findAllByStatusAndLabelsLike(@Param("status") Publish.STATUS status, @Param("labels") String labelsLike, Pageable pageable);

    Page<Publish> findAllByStatusAndTextLike(@Param("status") Publish.STATUS status, @Param("text") String textLike, Pageable pageable);

    Page<Publish> findAllByStatusAndUid(@Param("status") Publish.STATUS status, @Param("uid") Long uid, Pageable pageable);
}
