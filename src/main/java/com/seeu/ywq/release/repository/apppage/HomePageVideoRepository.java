package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.apppage.HomePageVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface HomePageVideoRepository extends JpaRepository<HomePageVideo, Long> {
    List<HomePageVideo> findAllByCategory(@Param("category") HomePageVideo.CATEGORY category);

    Page<HomePageVideo> findAllByUid(@Param("uid") Long uid, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "select v.id, v.category, v.title, v.uid, ul.nickname, ul.head_icon_url, v.view_num, v.create_time, img.id as img_id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url, vd.id as video_id, vd.cover_url, vd.src_url from ywq_page_video v join ywq_user_login ul on v.uid = ul.uid left join ywq_image img on v.cover_image_id = img.id left join ywq_video vd on v.video_id = vd.id where v.category = ?1", nativeQuery = true)
    List<Object[]> findThemByCategory(@Param("category") Integer category);

    @Transactional
    @Modifying
    @Query("update HomePageVideo v set v.viewNum = v.viewNum + 1 where v.id = :id")
    void viewItOnce(@Param("id") Long id);
}
