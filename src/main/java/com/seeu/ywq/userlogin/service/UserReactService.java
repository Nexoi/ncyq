package com.seeu.ywq.userlogin.service;

import com.seeu.ywq.release.dvo.apppage.SimpleUserVO;
import com.seeu.ywq.userlogin.model.UserLogin;

public interface UserReactService {
    STATUS likeMe(Long myUid, Long hisUid);

    STATUS cancelLikeMe(Long myUid, Long hisUid);

    Boolean exists(Long uid);

    UserLogin findOne(Long uid);

    UserLogin findByPhone(String phone);

    SimpleUserVO findOneAndTransferToVO(Long uid);

    String getPhone(Long uid);

    String getWeChatID(Long uid);

    /* save **/
    UserLogin save(UserLogin userLogin);


    public enum STATUS {
        success,
        exist,
        not_exist,
        contradiction // 矛盾
    }
}
