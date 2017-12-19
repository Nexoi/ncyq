package com.seeu.ywq.release.service.impl;

import com.seeu.system.qiniu.ImageUploadService;
import com.seeu.ywq.release.model.Identification;
import com.seeu.ywq.release.model.IdentificationApply;
import com.seeu.ywq.release.model.Image;
import com.seeu.ywq.release.model.User$Identification;
import com.seeu.ywq.release.repository.IdentificationApplyRepository;
import com.seeu.ywq.release.repository.IdentificationRepository;
import com.seeu.ywq.release.repository.User$IdentificationRepository;
import com.seeu.ywq.release.service.IdentificationService;
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
    private ImageUploadService imageUploadService;

    @Override
    public List<User$Identification> findAllAccessByUid(Long uid) {
        return user$IdentificationRepository.findAllByUidAndStatusNot(uid, User$Identification.STATUS.waitFor);
    }

    @Override
    public List<User$Identification> findAllByUid(Long uid) {
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
        Image fImage = imageUploadService.uploadWithGetFullInfo(frontImage);
        Image bImage = imageUploadService.uploadWithGetFullInfo(backImage);
        applyData.setFrontIdCardImage(fImage);
        applyData.setBackIdCardImage(bImage);
        return identificationApplyRepository.save(applyData);
    }

    @Override
    public IdentificationApply findApplyInfo(Long uid) {
        return identificationApplyRepository.findOne(uid);
    }
}
