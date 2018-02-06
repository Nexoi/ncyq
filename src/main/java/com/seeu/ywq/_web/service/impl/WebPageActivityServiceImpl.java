package com.seeu.ywq._web.service.impl;

import com.seeu.ywq._web.model.WebPageActivity;
import com.seeu.ywq._web.repository.WebPageActivityRepository;
import com.seeu.ywq._web.service.WebPageActivityService;
import com.seeu.ywq.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 06/02/2018
 * Time: 3:21 PM
 * Describe:
 */
@Service
public class WebPageActivityServiceImpl implements WebPageActivityService {
    @Resource
    private WebPageActivityRepository repository;

    @Override
    public WebPageActivity findOne(Long id) throws ResourceNotFoundException {
        WebPageActivity activity = repository.findOne(id);
        if (activity == null || activity.getDeleteFlag() != WebPageActivity.DELETE_FLAG.show)
            throw new ResourceNotFoundException("无此活动页面");
        return activity;
    }

    @Override
    public Page<WebPageActivity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public WebPageActivity save(String title, String htmlContent) {
        WebPageActivity activity = new WebPageActivity();
        activity.setDeleteFlag(WebPageActivity.DELETE_FLAG.show);
        activity.setHtmlContent(htmlContent);
        activity.setTitle(title);
        activity.setUpdateTime(new Date());
        return repository.save(activity);
    }

    @Override
    public WebPageActivity update(Long id, String title, String htmlContent) throws ResourceNotFoundException {
        WebPageActivity activity = findOne(id);
        if (activity == null)
            throw new ResourceNotFoundException("无此活动页面");
        activity.setUpdateTime(new Date());
        if (title != null) activity.setTitle(title);
        if (htmlContent != null) activity.setHtmlContent(htmlContent);
        return repository.save(activity);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        WebPageActivity activity = findOne(id);
        if (activity == null)
            throw new ResourceNotFoundException("无此活动页面");
        activity.setDeleteFlag(WebPageActivity.DELETE_FLAG.delete);
        repository.save(activity);
    }
}
