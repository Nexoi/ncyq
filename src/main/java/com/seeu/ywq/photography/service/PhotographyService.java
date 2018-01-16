package com.seeu.ywq.photography.service;

import com.seeu.ywq.photography.model.Photography;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotographyService {

    Page findAllByUid(Long uid, Pageable pageable);

    // 包括持久化操作
    List<Photography> save(Long uid, MultipartFile[] images) throws IOException;
}
