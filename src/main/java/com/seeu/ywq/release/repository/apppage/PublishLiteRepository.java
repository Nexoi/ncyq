package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.apppage.PublishLite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.SqlResultSetMapping;
import java.util.Collection;
import java.util.List;

public interface PublishLiteRepository extends JpaRepository<PublishLite, Long> {

    @Query(value = "SELECT p.id, p.uid, p.weight, p.type, p.title, p.create_time, p.unlock_price, p.view_num, p.comment_num, p.like_num, p.labels, p.text, v.id as videoId, v.cover_url, v.src_url FROM xy_ywq.ywq_user_tags ut right join ywq_publish p on p.uid = ut.user_uid left join ywq_video v on v.id = p.video_id where p.status = 0 and ut.tags_id in (:labels) group by p.id ORDER BY ?#{#pageable}", nativeQuery = true)
    List<Object[]> queryItUseMyTags(@Param("labels") Collection<Long> labels, Pageable pageable);

    @Query(value = "SELECT count( distinct p.id) FROM xy_ywq.ywq_publish p left join ywq_user_tags ut on p.uid = ut.user_uid where ut.tags_id in (:labels)", nativeQuery = true)
    Integer countItUseMyTags(@Param("labels") Collection<Long> labels);

    @Query(value = "SELECT p.id, p.uid, p.weight, p.type, p.title, p.create_time, p.unlock_price, p.view_num, p.comment_num, p.like_num, p.labels, p.text, v.id as videoId, v.cover_url, v.src_url FROM xy_ywq.ywq_fans f right join ywq_publish p on p.uid = f.followed_uid left join ywq_video v on v.id = p.video_id where p.status = 0 and f.followed_uid in (:labels) group by p.id ORDER BY ?#{#pageable}", nativeQuery = true)
    List<Object[]> queryItUseFollowedUids(@Param("labels") Collection<Long> labels, Pageable pageable);

    @Query(value = "SELECT count(*) FROM xy_ywq.ywq_publish p where p.status = 0 and p.uid in (:labels)", nativeQuery = true)
    Integer countItUseFollowedUids(@Param("labels") Collection<Long> labels);

    Page findAllByUid(@Param("uid") Long uid, Pageable pageable);
}
