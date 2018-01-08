package com.seeu.ywq.userlogin.service;

import com.seeu.ywq.page.dvo.SimpleUserVO;
import com.seeu.ywq.userlogin.dvo.UserLoginVO;
import com.seeu.ywq.userlogin.model.UserLogin;

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

    UserLogin findOne(Long uid);

    UserLoginVO findOneWithSafety(Long uid);

    UserLogin findByPhone(String phone);

    SimpleUserVO findOneAndTransferToVO(Long uid);

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
}
