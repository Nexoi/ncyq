package com.seeu.ywq.user.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.user.model.Identification;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.resource.model.Image;
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
import java.util.Date;
import java.util.List;

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
        return user$IdentificationRepository.findAllByUidAndStatusNot(uid, UserIdentification.STATUS.waitFor);
    }

    @Override
    public List<UserIdentification> findAllByUid(Long uid) {
        return user$IdentificationRepository.findAllByUid(uid);
    }

    @Override
    public List<Identification> findAll() {
        return identificationRepository.findAll();
    }

    @Override
    public IdentificationApply apply(Long uid, IdentificationApply applyData, MultipartFile frontImage, MultipartFile backImage) throws IOException {
        if (applyData == null) return null;
        applyData.setUid(uid);
        applyData.setCreateTime(new Date());
        // 上传图片
        Image fImage = fileUploadService.uploadImage(frontImage);
        Image bImage = fileUploadService.uploadImage(backImage);
        applyData.setFrontIdCardImage(fImage);
        applyData.setBackIdCardImage(bImage);
        return identificationApplyRepository.save(applyData);
    }

    @Override
    public IdentificationApply findApplyInfo(Long uid) {
        return identificationApplyRepository.findOne(uid);
    }
}
