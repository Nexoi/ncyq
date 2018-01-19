package com.seeu.ywq.page_video.repository;

import com.seeu.ywq.page_video.model.HomePageVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface HomePageVideoRepository extends JpaRepository<HomePageVideo, Long> {
    Page<HomePageVideo> findAllByCategory(@Param("category") HomePageVideo.CATEGORY category, Pageable pageable);

    Page<HomePageVideo> findAllByUid(@Param("uid") Long uid, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "select v.id, v.category, v.title, v.uid, ul.nickname, ul.head_icon_url, v.view_num, v.create_time," +
            " img.id as img_id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url," +
            " vd.id as video_id, vd.cover_url, vd.src_url, " +
            " v.diamonds, v.received_diamonds " +
            " from ywq_page_video v join ywq_user_login ul on v.uid = ul.uid" +
            " left join ywq_image img on v.cover_image_id = img.id" +
            " left join ywq_video vd on v.video_id = vd.id" +
            " where v.category = :category limit :startPage, :pageSize ", nativeQuery = true)
    List<Object[]> queryThem(@Param("category") Integer category,
                             @Param("startPage") Integer startPage,
                             @Param("pageSize") Integer pageSize);

    @Query(value = "select count(*) " +
            " from ywq_page_video v join ywq_user_login ul on v.uid = ul.uid" +
            " left join ywq_image img on v.cover_image_id = img.id" +
            " left join ywq_video vd on v.video_id = vd.id" +
            " where v.category = ?1 ", nativeQuery = true)
    Long countThem(@Param("category") Integer category);

    @Transactional
    @Modifying
    @Query("update HomePageVideo v set v.viewNum = v.viewNum + 1 where v.id = :id")
    void viewItOnce(@Param("id") Long id);
}
