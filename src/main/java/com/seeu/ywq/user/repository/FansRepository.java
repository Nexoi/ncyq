package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.Fans;
import com.seeu.ywq.user.model.FansPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FansRepository extends JpaRepository<Fans, FansPKeys> {

    // 找出该 fansUid 对应的所有 followedUid，相当于找到自己关注的所有人
    Page findAllByFansUidAndDeleteFlag(@Param("fansUid") Long fansUid, @Param("deleteFlag") Fans.DELETE_FLAG deleteFlag, Pageable pageable);

    // 找出该 followedUid 对应的所有 fansUid，相当于找到自己所有的粉丝
    Page findAllByFollowedUidAndDeleteFlag(@Param("followedUid") Long followedUid, @Param("deleteFlag") Fans.DELETE_FLAG deleteFlag, Pageable pageable);

    // full identification info
    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce," +
            " GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, " +
            " GROUP_CONCAT(idef.icon_active_url SEPARATOR '@@') identification_urls " + // 用{}区分开
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.followed_uid = ul.uid and ul.nickname like :word " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " left join ywq_identification idef on idef.id = iden.identification_id " +
            " where fans.fans_uid = :fansUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.fans_uid = :fansUid",
            nativeQuery = true)
    Page<Object[]> searchItByFansUidWithFullIdentificationInfo(@Param("fansUid") Long fansUid, @Param("word") String word, Pageable pageable);

    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce, " +
            " GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, " +
            " GROUP_CONCAT(idef.icon_active_url SEPARATOR '@@') identification_urls " + // 用{}区分开
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.followed_uid = ul.uid " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " left join ywq_identification idef on idef.id = iden.identification_id " +
            " where fans.fans_uid = :fansUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.fans_uid = :fansUid",
            nativeQuery = true)
    Page<Object[]> findItByFansUidWithFullIdentificationInfo(@Param("fansUid") Long fansUid, Pageable pageable);

    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce," +
            " GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, " +
            " GROUP_CONCAT(idef.icon_active_url SEPARATOR '@@') identification_urls " + // 用{}区分开
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.fans_uid = ul.uid " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " left join ywq_identification idef on idef.id = iden.identification_id " +
            " where fans.followed_uid = :followedUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.followed_uid = :followedUid",
            nativeQuery = true)
    Page<Object[]> findItByFollowedUidWithFullIdentificationInfo(@Param("followedUid") Long followedUid, Pageable pageable);

    // simple info

    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce," +
            " GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids " +
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.followed_uid = ul.uid and ul.nickname like :word " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " where fans.fans_uid = :fansUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.fans_uid = :fansUid",
            nativeQuery = true)
    Page<Object[]> searchItByFansUid(@Param("fansUid") Long fansUid, @Param("word") String word, Pageable pageable);

    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce, GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids " +
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.followed_uid = ul.uid " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " where fans.fans_uid = :fansUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.fans_uid = :fansUid",
            nativeQuery = true)
    Page<Object[]> findItByFansUid(@Param("fansUid") Long fansUid, Pageable pageable);


    @Query(value = "select fans.fans_uid, fans.followed_uid, fans.follow_each, ul.nickname, ul.head_icon_url, u.introduce, GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids " +
            " from ywq_fans fans " +
            " right join ywq_user_login as ul on fans.fans_uid = ul.uid " +
            " left join ywq_user_identifications as iden on iden.uid = ul.uid and iden.status = 0 " +
            " left join ywq_user as u on u.uid = ul.uid " +
            " where fans.followed_uid = :followedUid " +
            " group by ul.uid ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from ywq_fans fans where fans.followed_uid = :followedUid",
            nativeQuery = true)
    Page<Object[]> findItByFollowedUid(@Param("followedUid") Long followedUid, Pageable pageable);

    List<Fans> findAllByFansUid(@Param("fansUid") Long fansUid);

}
