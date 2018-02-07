package com.seeu.ywq.userlogin.service;

import com.seeu.ywq.api.admin.user.USERLogin;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.userlogin.dvo.UserLoginVO;
import com.seeu.ywq.userlogin.exception.NickNameSetException;
import com.seeu.ywq.userlogin.exception.PasswordSetException;
import com.seeu.ywq.userlogin.exception.PhoneNumberHasUsedException;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 辅助设计，对用户的基本交互（规避权限安全问题）
 */
public interface UserReactService {
    STATUS likeMe(Long myUid, Long hisUid);

    STATUS cancelLikeMe(Long myUid, Long hisUid);

    Boolean hasLikedHer(Long uid, Long herUid);

    BigDecimal calculateDistanceFromHer(BigDecimal longitude, BigDecimal latitude, Long herUid);

    BigDecimal calculateDistance(BigDecimal fromLongitude, BigDecimal fromLatitude, BigDecimal toLongitude, BigDecimal toLatitude);

    Boolean exists(Long uid);

    /**
     * 会返回所有信息
     *
     * @param uid
     * @return
     */
    UserLogin findOne(Long uid);

    UserLoginVO findOneWithSafety(Long uid);

    UserLogin findByPhone(String phone);

    /**
     * @param visitorUid 如果为空，返回基础信息
     * @param uid
     * @return
     */
    SimpleUserVO findOneAndTransferToVO(Long visitorUid, Long uid);

    List<SimpleUserVO> findAllSimpleUsers(Long uid, Collection<Long> uids);

    String getPhone(Long uid);

    String getWeChatID(Long uid);

    /* save **/
    UserLogin save(UserLogin userLogin);

    UserLogin saveNickName(Long uid, String nickname);

    public enum STATUS {
        success,
        exist,
        not_exist,
        contradiction // 矛盾
    }

    // search //

    Page<SimpleUserVO> search(Long visitorUid, String word, Pageable pageable);

    // admin //

    Page<UserLogin> findAll(Pageable pageable);

    Page<UserLogin> searchAll(USERLogin searchType, String word, Pageable pageable) throws ActionParameterException;

    UserLogin add(UserLogin userLogin, User user) throws NickNameSetException, PhoneNumberHasUsedException, PasswordSetException;

    Page<UserLogin> findAllByUids(Collection<Long> uids, Pageable pageable);
}
