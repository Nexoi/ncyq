package com.seeu.ywq.page.repository;

import com.seeu.ywq.page.model.PublishLite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PublishLiteRepository extends JpaRepository<PublishLite, Long> {

    @Query(value = "SELECT p.id, p.uid, p.weight, p.type, p.title, p.create_time, p.unlock_price, p.view_num, p.comment_num, p.like_num, p.labels, p.text, v.id as videoId, v.cover_url, v.src_url "+
            "FROM ywq_user_tags ut " +
            "right join ywq_publish p on p.uid = ut.user_uid " +
            "left join ywq_video v on v.id = p.video_id " +
            "where p.status = 0 and ut.tags_id in (:labels) " +
            "group by p.id " +
            "ORDER BY p.create_time desc limit :startPage, :pageSize", nativeQuery = true)
    List<Object[]> queryItUseMyTags(@Param("labels") Collection<Long> labels, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    @Query(value = "SELECT count( distinct p.id) " +
            "FROM ywq_publish p " +
            "left join ywq_user_tags ut on p.uid = ut.user_uid " +
            "where ut.tags_id in (:labels)", nativeQuery = true)
    Integer countItUseMyTags(@Param("labels") Collection<Long> labels);

    @Query(value = "SELECT p.id, p.uid, p.weight, p.type, p.title, p.create_time, p.unlock_price, p.view_num, p.comment_num, p.like_num, p.labels, p.text, v.id as videoId, v.cover_url, v.src_url " +
            "FROM ywq_fans f " +
            "right join ywq_publish p on p.uid = f.followed_uid " +
            "left join ywq_video v on v.id = p.video_id " +
            "where p.status = 0 and f.followed_uid in (:labels) " +
            "group by p.id ORDER BY p.create_time desc limit :startPage, :pageSize", nativeQuery = true)
    List<Object[]> queryItUseFollowedUids(@Param("labels") Collection<Long> labels, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    @Query(value = "SELECT count(*) " +
            "FROM ywq_publish p " +
            "where p.status = 0 and p.uid in (:labels)", nativeQuery = true)
    Integer countItUseFollowedUids(@Param("labels") Collection<Long> labels);

    Page findAllByUid(@Param("uid") Long uid, Pageable pageable);

}
