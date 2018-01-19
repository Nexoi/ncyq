package com.seeu.ywq.user.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.api.admin.user.USER;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dto.UserDTO;
import com.seeu.ywq.user.dvo.UserIdentificationVO;
import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.model.UserIdentification;
import com.seeu.ywq.user.repository.UserInfoRepository;
import com.seeu.ywq.user.service.IdentificationService;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private UserReactService userReactService;
    @Autowired
    private IdentificationService identificationService;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public User findOneInfo(Long uid) {
        return userInfoRepository.findOne(uid);
    }

    @Override
    public User saveInfo(User user) {
        return userInfoRepository.save(user);
    }

    @Override
    public User saveInfo(Long uid, UserDTO userDTO) {
        if (userDTO == null) return null;
        User user = userInfoRepository.findOne(uid);
        if (userDTO.getBirthDay() != null)
            user.setBirthDay(userDTO.getBirthDay());
        if (userDTO.getBust() != null)
            user.setBust(userDTO.getBust());
        if (userDTO.getHeight() != null)
            user.setHeight(userDTO.getHeight());
        if (userDTO.getHip() != null)
            user.setHip(userDTO.getHip());
        if (userDTO.getIntroduce() != null)
            user.setIntroduce(userDTO.getIntroduce());
        if (userDTO.getStarSign() != null)
            user.setStarSign(userDTO.getStarSign());
        if (userDTO.getWaist() != null)
            user.setWaist(userDTO.getWaist());
        if (userDTO.getWechat() != null)
            user.setWechat(userDTO.getWechat());
        if (userDTO.getWeight() != null)
            user.setWeight(userDTO.getWeight());
        return userInfoRepository.save(user);
    }

    @Override
    public UserVO findOne(Long uid) {
        User user = userInfoRepository.findOne(uid);
        UserVO vo = transferToVO(user);
        if (vo != null) {
            List<UserIdentification> identifications = identificationService.findAllAccessByUid(user.getUid());
            vo.setIdentifications(transferToVO(identifications));
        }
        return vo;
    }

    @Override
    public void followPlusOne(Long uid) {
        userInfoRepository.followPlusOne(uid);
    }

    @Override
    public void followMinsOne(Long uid) {
        userInfoRepository.followMinsOne(uid);
    }

    @Override
    public void fansPlusOne(Long uid) {
        userInfoRepository.fansPlusOne(uid);
    }

    @Override
    public void fansMinsOne(Long uid) {
        userInfoRepository.fansMinsOne(uid);
    }

    @Override
    public void publishPlusOne(Long uid) {
        userInfoRepository.publishPlusOne(uid);
    }

    @Override
    public void publishMinsOne(Long uid) {
        userInfoRepository.publishMinsOne(uid);
    }

    @Override
    public void likeMePlusOne(Long uid) {
        userInfoRepository.likeMe(uid);
    }

    @Override
    public void likeMeMinsOne(Long uid) {
        userInfoRepository.cancelLikeMe(uid);
    }

    @Override
    public String updateHeadIcon(Long uid, MultipartFile image) {
        UserLogin ul = userReactService.findOne(uid);
        if (ul == null)
            return null;
        try {
            Image imageModel = fileUploadService.uploadImage(image);
            String url = imageModel.getImageUrl();
            ul.setHeadIconUrl(url);
            userReactService.save(ul);
            return url;
        } catch (Exception e) {
            // ..
        }
        return null;
    }

    @Override
    public UserLogin setGender(Long uid, UserLogin.GENDER gender) throws ActionParameterException, ActionNotSupportException, ResourceNotFoundException {
        if (gender == null)
            throw new ActionParameterException("gender");
        UserLogin ul = userReactService.findOne(uid);
        if (ul == null)
            throw new ResourceNotFoundException("Can not found Resource [UID:" + uid + "]");
        if (ul.getGender() != null)
            throw new ActionNotSupportException("性别已经设定了！不可修改");
        ul.setGender(gender);
        return userReactService.save(ul);
    }

    private UserVO transferToVO(User user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
//        List<TagVO> tagVOS = new ArrayList<>();
//        List<Tag> tags = user.getTags();
//        if (tags == null || tags.size() == 0) return vo;
//        for (Tag tag : tags) {
//            TagVO tagVO = new TagVO();
//            tagVO.setTagName(tag.getTagName());
//            tagVO.setId(tag.getId());
//            tagVOS.add(tagVO);
//        }
//        vo.setTags(tagVOS);
        return vo;
    }

    private UserIdentificationVO transferToVO(UserIdentification identification) {
        if (identification == null) return null;
        UserIdentificationVO identificationVO = new UserIdentificationVO();
        BeanUtils.copyProperties(identification, identificationVO);
        return identificationVO;
    }

    private List<UserIdentificationVO> transferToVO(List<UserIdentification> identifications) {
        if (identifications == null || identifications.size() == 0) return new ArrayList<>();
        List<UserIdentificationVO> vos = new ArrayList<>();
        for (UserIdentification identification : identifications) {
            vos.add(transferToVO(identification));
        }
        return vos;
    }


    // admin //


    @Override
    public Page<User> findAll(Pageable pageable) {
        return userInfoRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchAll(USER searchBy, String word, Pageable pageable) {
        if (searchBy == null) return findAll(pageable);
        if (searchBy == USER.phone) return userInfoRepository.findAllByPhoneLike("%" + word + "%", pageable);
        if (searchBy == USER.wechat) return userInfoRepository.findAllByWechatLike("%" + word + "%", pageable);
        if (searchBy == USER.introduce) return userInfoRepository.findAllByIntroduceLike("%" + word + "%", pageable);
        return findAll(pageable);
    }
}
