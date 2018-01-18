package com.seeu.ywq.page_advertisement.service;

import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page_advertisement.model.Advertisement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertisementService {

    void addAdvertisement(MultipartFile image, Advertisement.CATEGORY category, String url, Integer order) throws ResourceAddException;

    void deleteAdvertisement(Long advertisementId) throws ResourceNotFoundException;

    List<Advertisement> getAdvertisements(Advertisement.CATEGORY category);
}
