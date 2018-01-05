package com.seeu.ywq.user.service;

import com.seeu.ywq.user.model.Identification;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.UserIdentification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IdentificationService {
    List<UserIdentification> findAllAccessByUid(Long uid);

    List<UserIdentification> findAllByUid(Long uid); // 所有的信息，不管审核通过没，一般给自己使用

    List<Identification> findAll(); // 全部在运营的列表

    IdentificationApply apply(Long uid, IdentificationApply applyData, MultipartFile frontImage, MultipartFile backImage) throws IOException;

    IdentificationApply findApplyInfo(Long uid); // 一个用户只有一份资料
}
