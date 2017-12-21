package com.seeu.ywq.userlogin.service;

public interface UserReactService {
    STATUS likeMe(Long myUid, Long hisUid);

    STATUS cancelLikeMe(Long myUid, Long hisUid);

    public enum STATUS {
        success,
        exist,
        not_exist,
        contradiction // 矛盾
    }
}
