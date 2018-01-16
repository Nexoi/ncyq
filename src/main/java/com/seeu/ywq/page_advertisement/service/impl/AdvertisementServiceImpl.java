package com.seeu.ywq.page_advertisement.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.repository.AdvertisementRepository;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Resource
    private AdvertisementRepository repository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ImageService imageService;

    @Override
    public void addAdvertisement(MultipartFile imageFile, Advertisement.CATEGORY category, String url, Integer orderId) throws ResourceAddException {
        // 文件上传
        try {
            Image image = fileUploadService.uploadImage(imageFile);
            image = imageService.save(image); // 持久化（先持久化才能被 set 进去。。）
            // 持久化
            Advertisement advertisement = new Advertisement();
            advertisement.setImage(image);
            advertisement.setUrl(url);
            advertisement.setCategory(category);
            advertisement.setOrderId(orderId);
            advertisement.setCreateTime(new Date());
            repository.save(advertisement);
        } catch (Exception e) {
            throw new ResourceAddException(e.getMessage());
        }
    }

    @Override
    public void deleteAdvertisement(Long advertisementId) throws ResourceNotFoundException {
        Advertisement advertisement = repository.findOne(advertisementId);
        if (advertisement == null)
            throw new ResourceNotFoundException("Can not found Advertisement[ID:" + advertisementId + "]");
        repository.delete(advertisementId);
    }

    @Override
    public List<Advertisement> getAdvertisements(Advertisement.CATEGORY category) {
        return repository.findAllByCategory(category);
    }
}
