package com.seeu.ywq._web.service;

import com.seeu.ywq._web.model.WebPageActivity;
import com.seeu.ywq.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Created by suneo.
 * User: neo
 * Date: 06/02/2018
 * Time: 3:15 PM
 * Describe:
 */

public interface WebPageActivityService {
    WebPageActivity findOne(Long id) throws ResourceNotFoundException;

    Page<WebPageActivity> findAll(Pageable pageable);

    WebPageActivity save(String title, String htmlContent);

    WebPageActivity update(Long id, String title, String htmlContent) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
