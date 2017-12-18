package com.seeu.ywq.release.service;

import com.seeu.ywq.release.model.User$Identification;

import java.util.List;

public interface User$IdentificationService {
    List<User$Identification> findAllAccessByUid(Long uid);
}
