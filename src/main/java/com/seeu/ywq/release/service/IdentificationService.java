package com.seeu.ywq.release.service;

import com.seeu.ywq.release.model.Identification;
import com.seeu.ywq.release.model.IdentificationApply;
import com.seeu.ywq.release.model.User$Identification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IdentificationService {
    List<User$Identification> findAllAccessByUid(Long uid);

    List<User$Identification> findAllByUid(Long uid); // 所有的信息，不管审核通过没，一般给自己使用

    List<Identification> findAll(); // 全部在运营的列表

    IdentificationApply apply(Long uid, IdentificationApply applyData, MultipartFile frontImage, MultipartFile backImage) throws IOException;
}
