package com.seeu.ywq.user.service;

import com.seeu.ywq.exception.IdentificationApplyRepeatException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dvo.UserIdentificationWithFullListVO;
import com.seeu.ywq.user.model.Identification;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.model.UserIdentification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IdentificationService {
    List<UserIdentification> findAllAccessByUid(Long uid); // 查看所有通过了的认证信息

    List<UserIdentification> findAllByUid(Long uid); // 查看自己的信息（各种状态都有）

    List<UserIdentificationWithFullListVO> findAllWithFullIdentificationInfoByUid(Long uid); // 所有的信息，不管审核通过没，一般给自己使用

    List<Identification> findAll(); // 全部在运营的列表

    IdentificationApply apply(Long identificationId, Long uid, IdentificationApply applyData, MultipartFile frontImage, MultipartFile backImage, MultipartFile transferVoucherImage) throws IOException, IdentificationApplyRepeatException, ResourceNotFoundException;

    IdentificationApply findApplyInfo(Long uid, Long identificationId); // 一个用户某一个申请信息

    IdentificationApply findMyRecentInfo(Long uid); // 查看自己最近一次上传的信息（去掉流水号、转账信息等）

    void deleteApply(Long uid, Long identificationId) throws ResourceNotFoundException;
}
