package com.seeu.ywq.page_home.repository;

import com.seeu.ywq.page_home.model.HomeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 4:19 PM
 * Describe:
 */

public interface HomeUserRepository extends JpaRepository<HomeUser, Long> {

    // 默认按照时间排序，// 【update！不考虑随机】加入随机机制（ uid % num == 0 即可）
    @Query(value = "select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids, " +
            " hu.cover_image_url, hu.label, hu.create_time " +
            " if(ulike.uid is null,0,1) as likeher " +
            "from ywq_page_home_users hu " +
            "left join ywq_user_login ul on hu.uid = ul.uid " +
            "left join ywq_user us on us.uid = ul.uid " +
            "left join ywq_user_identifications iden on iden.uid = ul.uid " +
            "left join ywq_user_like ulike on ulike.liked_uid = ul.uid and ulike.uid = :uid " +
            "where hu.delete_flag = 0 " +
            "group by hu.uid " +
            "order by hu.create_time desc " +
            "limit :startPage, :pageSize ", nativeQuery = true)
    List<Object[]> queryPage(@Param("uid") Long uid,
//                             @Param("randomNumber") Integer randomNumber,
                             @Param("startPage") Integer startPage,
                             @Param("pageSize") Integer pageSize);

    // 不需要加入用户个人影响
    @Query(value = "select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids, " +
            " hu.cover_image_url, hu.label, hu.create_time " +
            "from ywq_page_home_users hu " +
            "left join ywq_user_login ul on hu.uid = ul.uid " +
            "left join ywq_user us on us.uid = ul.uid " +
            "left join ywq_user_identifications iden on iden.uid = ul.uid " +
            "where hu.delete_flag = 0 " +
            "group by hu.uid " +
            "order by hu.create_time desc " +
            "limit :startPage, :pageSize ", nativeQuery = true)
    List<Object[]> queryPage(@Param("startPage") Integer startPage,
                             @Param("pageSize") Integer pageSize);


    @Query(value = "select count(*) " +
            "from ywq_page_home_users hu " +
            "where hu.delete_flag = 0 ",
            nativeQuery = true)
    Integer countPage();
}
