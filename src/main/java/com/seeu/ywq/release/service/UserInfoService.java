package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.UserVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {
    UserVO findOne(Long uid);

    /**
     * @param uid
     * @param image
     * @return 返回头像地址，若为 null 表示更新失败
     */
    String updateHeadIcon(Long uid, MultipartFile image);

    STATUS setGender(Long uid, UserLogin.GENDER gender);

    public enum STATUS {
        has_set,
        success,
        failure
    }
}
