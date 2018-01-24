package com.seeu.ywq.page_home.service;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page_home.model.HomeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 4:23 PM
 * Describe:
 */

public interface HomeUserService {
    Page<HomeUser> queryAll(Long uid, Pageable pageable);

    Page<HomeUser> queryAll(Pageable pageable);


    Page<HomeUser> queryAllByLABEL(Long uid, HomeUser.LABEL label, Pageable pageable);

    Page<HomeUser> queryAllByLABEL(HomeUser.LABEL label, Pageable pageable);

    // admin //

    Page<HomeUser> findAll(Pageable pageable);

    HomeUser findOne(Long uid) throws ResourceNotFoundException;

    HomeUser save(Long uid, HomeUser.LABEL label, String coverImageUrl, String videoUrl) throws ActionParameterException, ResourceAlreadyExistedException;

    void delete(Long uid) throws ResourceNotFoundException; // deleteFlag

    HomeUser update(Long uid, HomeUser.LABEL label, String coverImageUrl, String videoUrl) throws ResourceNotFoundException;
}
