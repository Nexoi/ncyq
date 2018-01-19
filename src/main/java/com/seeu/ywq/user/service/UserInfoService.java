package com.seeu.ywq.user.service;

import com.seeu.ywq.api.admin.user.USER;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dto.UserDTO;
import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    User findOneInfo(Long uid);

    User saveInfo(User user);

    User saveInfo(Long uid, UserDTO userDTO);

    UserVO findOne(Long uid);

    void followPlusOne(Long uid);

    void followMinsOne(Long uid);

    void fansPlusOne(Long uid);

    void fansMinsOne(Long uid);

    void publishPlusOne(Long uid);

    void publishMinsOne(Long uid);

    void likeMePlusOne(Long uid);

    void likeMeMinsOne(Long uid);

    /**
     * @param uid
     * @param image
     * @return 返回头像地址，若为 null 表示更新失败
     */
    String updateHeadIcon(Long uid, MultipartFile image);

    UserLogin setGender(Long uid, UserLogin.GENDER gender) throws ActionParameterException, ActionNotSupportException, ResourceNotFoundException;

    // admin //

    Page<User> findAll(Pageable pageable);

    Page<User> searchAll(USER searchBy, String word, Pageable pageable);

}
