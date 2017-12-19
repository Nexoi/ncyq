package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.dvo.FansVO;
import com.seeu.ywq.release.model.Fans;
import com.seeu.ywq.release.model.FansPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FansRepository extends JpaRepository<Fans, FansPKeys> {

    // 找出该 fansUid 对应的所有 followedUid，相当于找到自己关注的所有人
    Page findAllByFansUidAndDeleteFlag(@Param("fansUid") Long fansUid, @Param("deleteFlag") Fans.DELETE_FLAG deleteFlag, Pageable pageable);

    // 找出该 followedUid 对应的所有 fansUid，相当于找到自己所有的粉丝
    Page findAllByFollowedUidAndDeleteFlag(@Param("followedUid") Long followedUid, @Param("deleteFlag") Fans.DELETE_FLAG deleteFlag, Pageable pageable);

    //
    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, pu.text, GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids from ywq_fans fans join ywq_user_login as ul on fans.followed_uid = ul.uid left join ywq_publish as pu on pu.uid = fans.followed_uid left join ywq_user_identifications as iden on iden.uid = fans.followed_uid where fans.fans_uid = :fansUid group by fans.followed_uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.fans_uid = :fansUid",
            nativeQuery = true)
    Page<Object[]> findItByFansUid(@Param("fansUid") Long fansUid, Pageable pageable);

    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, pu.text, GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids from ywq_fans fans join ywq_user_login as ul on fans.fans_uid = ul.uid left join ywq_publish as pu on pu.uid = fans.fans_uid left join ywq_user_identifications as iden on iden.uid = fans.fans_uid where fans.followed_uid = :followedUid group by fans.fans_uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.followed_uid = :followedUid",
            nativeQuery = true)
    Page<Object[]> findItByFollowedUid(@Param("followedUid") Long followedUid, Pageable pageable);
}
