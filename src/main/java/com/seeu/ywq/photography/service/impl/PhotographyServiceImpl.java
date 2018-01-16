package com.seeu.ywq.photography.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.photography.model.Photography;
import com.seeu.ywq.photography.repository.PhotographyRepository;
import com.seeu.ywq.photography.service.PhotographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PhotographyServiceImpl implements PhotographyService {
    @Resource
    private PhotographyRepository repository;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public Page findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUidAndDeleteFlag(uid, Photography.DELETE_FLAG.show, pageable);
    }

    @Override
    public List<Photography> save(Long uid, MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) return new ArrayList<>();
        List<Photography> photographies = new ArrayList<>();
        // 上传文件
        for (MultipartFile file : images) {
            Image image = fileUploadService.uploadImage(file);
            Photography photography = new Photography();
            photography.setCreateTime(new Date());
            photography.setDeleteFlag(Photography.DELETE_FLAG.show);
            photography.setImage(image);
            photography.setUid(uid);
            photographies.add(photography);
        }
        return repository.save(photographies);
    }
}
