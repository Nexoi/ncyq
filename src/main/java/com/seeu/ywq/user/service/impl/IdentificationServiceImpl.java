package com.seeu.ywq.user.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.exception.IdentificationApplyRepeatException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dvo.UserIdentificationWithFullListVO;
import com.seeu.ywq.user.model.Identification;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.user.model.IdentificationApplyPKeys;
import com.seeu.ywq.user.model.UserIdentification;
import com.seeu.ywq.user.repository.IdentificationApplyRepository;
import com.seeu.ywq.user.repository.IdentificationRepository;
import com.seeu.ywq.user.repository.User$IdentificationRepository;
import com.seeu.ywq.user.service.IdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class IdentificationServiceImpl implements IdentificationService {


    @Resource
    private IdentificationRepository identificationRepository;
    @Resource
    private User$IdentificationRepository user$IdentificationRepository;
    @Resource
    private IdentificationApplyRepository identificationApplyRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public List<UserIdentification> findAllAccessByUid(Long uid) {
        return user$IdentificationRepository.findAllByUidAndStatus(uid, UserIdentification.STATUS.active);
    }

    @Override
    public List<UserIdentification> findAllByUid(Long uid) {
        return user$IdentificationRepository.findAllByUid(uid);
    }

    @Override
    public List<UserIdentificationWithFullListVO> findAllWithFullIdentificationInfoByUid(Long uid) {
        List<UserIdentification> list = user$IdentificationRepository.findAllByUid(uid);
        List<Identification> identifications = identificationRepository.findAll();
        List<UserIdentificationWithFullListVO> vos = new ArrayList<>();
        // hash
        Map<Long, UserIdentification> map = new HashMap();
        for (UserIdentification identification : list) {
            map.put(identification.getIdentificationId(), identification);
        }

        for (Identification identification : identifications) {
            UserIdentificationWithFullListVO vo = new UserIdentificationWithFullListVO();
            vo.setIdentificationId(identification.getId());
            vo.setIdentificationName(identification.getIdentificationName());
            vo.setIconUrl(identification.getIconUrl());
            vo.setIconActiveUrl(identification.getIconActiveUrl());
            UserIdentification userIdentification = map.get(identification.getId());
            if (userIdentification != null) {
                vo.setCreateTime(userIdentification.getCreateTime());
                vo.setStatus(userIdentification.getStatus());
            } else {
                vo.setStatus(UserIdentification.STATUS.inactive);
            }
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<Identification> findAll() {
        return identificationRepository.findAll();
    }

    @Override
    public IdentificationApply apply(Long identificationId, Long uid, IdentificationApply applyData, MultipartFile frontImage, MultipartFile backImage, MultipartFile transferVoucherImage) throws IOException, IdentificationApplyRepeatException, ResourceNotFoundException {
        if (applyData == null) return null;
        if (!identificationRepository.exists(identificationId))
            throw new ResourceNotFoundException("认证信息不存在");
        if (identificationApplyRepository.exists(new IdentificationApplyPKeys(identificationId, uid)))
            throw new IdentificationApplyRepeatException();
        applyData.setIdentificationId(identificationId);
        applyData.setUid(uid);
        applyData.setCreateTime(new Date());
        // 上传图片
        Image fImage = fileUploadService.uploadImage(frontImage);
        Image bImage = fileUploadService.uploadImage(backImage);
        Image transferImage = null;
        if (transferVoucherImage != null) // 用户可能不上传这个
            transferImage = fileUploadService.uploadImage(transferVoucherImage);
        applyData.setFrontIdCardImage(fImage);
        applyData.setBackIdCardImage(bImage);
        applyData.setTransferVoucherImage(transferImage);
        return identificationApplyRepository.save(applyData);
    }

    @Override
    public IdentificationApply findApplyInfo(Long uid, Long identificationId) {
        return identificationApplyRepository.findOne(new IdentificationApplyPKeys(identificationId, uid));
    }

    @Override
    public IdentificationApply findMyRecentInfo(Long uid) {
        return identificationApplyRepository.findFirstByUidOrderByCreateTimeDesc(uid);
    }

    @Override
    public void deleteApply(Long uid, Long identificationId) throws ResourceNotFoundException {
        // 查找
        IdentificationApplyPKeys pKeys = new IdentificationApplyPKeys(identificationId, uid);
        if (!identificationApplyRepository.exists(pKeys))
            throw new ResourceNotFoundException("资源不存在或未进行该认证申请");
        identificationApplyRepository.delete(pKeys);
    }
}
