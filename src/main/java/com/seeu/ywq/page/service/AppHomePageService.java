package com.seeu.ywq.page.service;

import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.dvo.HomePageVOUser;
import com.seeu.ywq.page.model.HomePageCategory;

import java.util.List;

/**
 * APP 首页搜索、列表
 */
public interface AppHomePageService {

    void addUserConfigurer(Integer category, Long srcId, Integer order) throws ResourceAlreadyExistedException;

    void deleteUserConfigurer(Integer category, Long uid) throws ResourceNotFoundException;

    // 查询该页信息
    List<HomePageCategory> queryAllByPage(HomePageCategory.PAGE page);

    List<HomePageCategory> queryAllByPage(Long visitorUid, HomePageCategory.PAGE page);

    List<HomePageVOUser> getHomePageUsers(Integer category);

    List<HomePageVOUser> getHomePageUsers(Long visitorUid, Integer category);

}
