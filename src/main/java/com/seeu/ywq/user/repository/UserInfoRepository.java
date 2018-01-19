package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserInfoRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("update User u set u.followNum = u.followNum + 1 where u.uid = :uid")
    void followPlusOne(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("update User u set u.followNum = u.followNum - 1 where u.uid = :uid")
    void followMinsOne(@Param("uid") Long uid);


    @Transactional
    @Modifying
    @Query("update User u set u.fansNum = u.fansNum + 1 where u.uid = :uid")
    void fansPlusOne(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("update User u set u.fansNum = u.fansNum - 1 where u.uid = :uid")
    void fansMinsOne(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("update User u set u.publishNum = u.publishNum + 1 where u.uid = :uid")
    void publishPlusOne(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("update User u set u.publishNum = u.publishNum - 1 where u.uid = :uid")
    void publishMinsOne(@Param("uid") Long uid);


    @Transactional
    @Modifying
    @Query("update User u set u.likeNum = u.likeNum + 1 where u.uid = :uid")
    void likeMe(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("update User u set u.likeNum = u.likeNum - 1 where u.uid = :uid")
    void cancelLikeMe(@Param("uid") Long uid);


    // admin //
    Page<User> findAllByPhoneLike(@Param("phone") String phone, Pageable pageable);

    Page<User> findAllByWechatLike(@Param("wechat") String wechat, Pageable pageable);

    Page<User> findAllByIntroduceLike(@Param("introduce") String introduce, Pageable pageable);
}
