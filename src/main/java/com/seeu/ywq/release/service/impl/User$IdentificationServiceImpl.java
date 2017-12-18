package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.model.User$Identification;
import com.seeu.ywq.release.repository.User$IdentificationRepository;
import com.seeu.ywq.release.service.User$IdentificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class User$IdentificationServiceImpl implements User$IdentificationService {
    @Resource
    private User$IdentificationRepository user$IdentificationRepository;

    @Override
    public List<User$Identification> findAllAccessByUid(Long uid) {
        return user$IdentificationRepository.findAllByUidAndStatusNot(uid, User$Identification.STATUS.waitFor);
    }
}
