package com.seeu.ywq.userlogin.repository;

import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    UserLogin findByPhone(@Param("phone") String phone);

    /**
     * 某人查找一组用户，该用户包含：VIP、个人认证、是否关注信息
     *
     * @param uid
     * @param uids
     * @return
     */
    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url, ul.gender, vip.vip_level, vip.termination_date, GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, " +
            " if(f.delete_flag=0,'1','0') as followed, " +
            " if(l.uid is null,'0','1') as liked " +
            "from ywq_user_login ul  " +
            "left join ywq_user_identifications iden on ul.uid = iden.uid and iden.status = 0 " +
            "left join ywq_user_vip vip on vip.uid=null or ul.uid=vip.uid " +
            "left join ywq_fans f on f.followed_uid = ul.uid and f.fans_uid = :uid " +
            "left join ywq_user_like l on l.liked_uid = ul.uid and l.uid = :uid " +
            "where ul.uid in (:uids) " +
            "group by ul.uid;", nativeQuery = true)
    List<Object[]> queryItsByUid(@Param("uid") Long uid, @Param("uids") Collection<Long> uids);

}