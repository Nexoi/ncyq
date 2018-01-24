//package com.seeu.ywq.page.repository;
//
//import com.seeu.ywq.page.model.HomePageUser;
//import com.seeu.ywq.page.model.HomePageUserPKeys;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface HomePageUserRepository extends JpaRepository<HomePageUser, HomePageUserPKeys> {
////    @Query(value = "select new Publish( ) from HomePageUser cfg, Publish p where cfg.category = :category and cfg.srcId = p.id")
////    List<Publish> findXXByCategoryFromConfig(@Param("category") Integer category);
//
////    @Query(value = "select p.* from ywq_page_configurer cfg, ywq_publish p where cfg.category = :category and cfg.uid = p.id", nativeQuery = true)
////    List<Publish> findXXByCategoryFromConfig(@Param("category") Integer category);
//
//    @Query(value = "select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids,  " +
//            " img.id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url  " +
//            " from ywq_user_login ul   " +
//            " left join ywq_user_identifications iden on iden.uid = ul.uid  " +
//            " left join ywq_user us on us.uid = ul.uid  " +
//            " left join ywq_photo_wall pw on pw.uid = ul.uid  " +
//            " left join ywq_image img on img.id = pw.image_id  " +
//            " where ul.uid = :uid group by iden.uid", nativeQuery = true)
//    Object[] findUserVOByUid(@Param("uid") Long uid);
//
//    @Query(value = "select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids, img.id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url " +
//            "from ywq_page_person pfg " +
//            "left join ywq_user_login ul on pfg.uid = ul.uid " +
//            "left join ywq_user us on us.uid = ul.uid " +
//            "left join ywq_user_identifications iden on iden.uid = ul.uid " +
//            "left join ywq_photo_wall pw on pw.uid = ul.uid " +
//            "left join ywq_image img on img.id = pw.image_id " +
//            "where pfg.category = ?1 " +
//            "group by iden.uid  " +
//            "order by pfg.order_id", nativeQuery = true)
//    List<Object[]> findUserVOByCategory(@Param("category") Integer category);
//
//    @Query(value = "select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids, img.id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url, " +
//            " if(ulike.uid is null,0,1) as likeher " +
//            "from ywq_page_person pfg " +
//            "left join ywq_user_login ul on pfg.uid = ul.uid " +
//            "left join ywq_user us on us.uid = ul.uid " +
//            "left join ywq_user_identifications iden on iden.uid = ul.uid " +
//            "left join ywq_photo_wall pw on pw.uid = ul.uid " +
//            "left join ywq_image img on img.id = pw.image_id " +
//            "left join ywq_user_like ulike on ulike.liked_uid = ul.uid and ulike.uid = ?1 " +
//            "where pfg.category = ?2 " +
//            "group by iden.uid  " +
//            "order by pfg.order_id", nativeQuery = true)
//    List<Object[]> findUserVOByCategory(@Param("visitorUid") Long visitorUid, @Param("category") Integer category);
//
//}
