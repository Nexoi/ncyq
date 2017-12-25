package com.seeu.ywq.release.service.impl;

import com.seeu.third.qiniu.FileUploadService;
import com.seeu.ywq.release.dvo.TagVO;
import com.seeu.ywq.release.dvo.User$IdentificationVO;
import com.seeu.ywq.release.dvo.UserVO;
import com.seeu.ywq.release.model.Image;
import com.seeu.ywq.release.model.Tag;
import com.seeu.ywq.release.model.User;
import com.seeu.ywq.release.model.User$Identification;
import com.seeu.ywq.release.repository.UserInfoRepository;
import com.seeu.ywq.release.service.IdentificationService;
import com.seeu.ywq.release.service.UserInfoService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserVO findOne(Long uid) {
        User user = userInfoRepository.findOne(uid);
        UserVO vo = transferToVO(user);
        if (vo != null) {
            List<User$Identification> identifications = identificationService.findAllAccessByUid(user.getUid());
            vo.setIdentifications(transferToVO(identifications));
        }
        return vo;
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
    public STATUS setGender(Long uid, UserLogin.GENDER gender) {
        UserLogin ul = userReactService.findOne(uid);
        if (ul == null)
            return STATUS.failure;
        if (ul.getGender() != null)
            return STATUS.has_set;
        ul.setGender(gender);
        userReactService.save(ul);
        return STATUS.success;
    }

    private UserVO transferToVO(User user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        List<TagVO> tagVOS = new ArrayList<>();
        List<Tag> tags = user.getTags();
        if (tags == null || tags.size() == 0) return vo;
        for (Tag tag : tags) {
            TagVO tagVO = new TagVO();
            tagVO.setTagName(tag.getTagName());
            tagVO.setId(tag.getId());
            tagVOS.add(tagVO);
        }
        vo.setTags(tagVOS);
        return vo;
    }

    private User$IdentificationVO transferToVO(User$Identification identification) {
        if (identification == null) return null;
        User$IdentificationVO identificationVO = new User$IdentificationVO();
        BeanUtils.copyProperties(identification, identificationVO);
        return identificationVO;
    }

    private List<User$IdentificationVO> transferToVO(List<User$Identification> identifications) {
        if (identifications == null || identifications.size() == 0) return new ArrayList<>();
        List<User$IdentificationVO> vos = new ArrayList<>();
        for (User$Identification identification : identifications) {
            vos.add(transferToVO(identification));
        }
        return vos;
    }
}
