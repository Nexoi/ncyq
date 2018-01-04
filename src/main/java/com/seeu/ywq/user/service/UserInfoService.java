package com.seeu.ywq.user.service;

import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    User findOneInfo(Long uid);

    User saveInfo(User user);

    UserVO findOne(Long uid);

    void followPlusOne(Long uid);

    void followMinsOne(Long uid);

    void fansPlusOne(Long uid);

    void fansMinsOne(Long uid);

    void publishPlusOne(Long uid);

    void publishMinsOne(Long uid);

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
