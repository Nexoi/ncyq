package com.seeu.ywq.page_home.service.impl;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page_home.model.HomeUser;
import com.seeu.ywq.page_home.repository.HomeUserRepository;
import com.seeu.ywq.page_home.service.HomeUserService;
import com.seeu.ywq.utils.AppVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 4:27 PM
 * Describe:
 */
@Service
public class HomeUserServiceImpl implements HomeUserService {
    @Resource
    private HomeUserRepository repository;
    @Autowired
    private AppVOUtils appVOUtils;

    @Override
    public Page<HomeUser> queryAll(Long uid, Pageable pageable) {
        List<Object[]> list = null;
        if (uid != null)
            list = repository.queryPage(uid, pageable.getPageNumber(), pageable.getPageSize());
        else
            list = repository.queryPage(pageable.getPageNumber(), pageable.getPageSize());
        if (list == null || list.size() == 0) return new PageImpl<>(new ArrayList<>());
        // counts
        int totalEls = repository.countPage();
        // transfer
        List<HomeUser> transferList = transferToHomeUser(list);
        return new PageImpl<>(transferList, pageable, totalEls);
    }

    @Override
    public Page<HomeUser> queryAll(Pageable pageable) {
        return queryAll(null, pageable);
    }

    @Override
    public Page<HomeUser> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public HomeUser findOne(Long uid) throws ResourceNotFoundException {
        if (uid == null) throw new ResourceNotFoundException("找不到该配置");
        HomeUser user = repository.findOne(uid);
        if (user == null) throw new ResourceNotFoundException("找不到该配置");
        return user;
    }

    @Override
    public void delete(Long uid) throws ResourceNotFoundException {
        if (uid == null) throw new ResourceNotFoundException("找不到该配置");
        HomeUser homeUser = repository.findOne(uid);
        if (null == homeUser) throw new ResourceNotFoundException("找不到该配置");
        homeUser.setDeleteFlag(HomeUser.DELETE.delete);
        repository.save(homeUser);
    }

    @Override
    public HomeUser update(Long uid, HomeUser.LABEL label, String coverImageUrl) throws ResourceNotFoundException {
        HomeUser user = repository.findOne(uid);
        if (null == user) throw new ResourceNotFoundException("找不到该配置");
        if (label != null) user.setLabel(label);
        if (coverImageUrl != null) user.setCoverImageUrl(coverImageUrl);
        return repository.save(user);
    }

    @Override
    public HomeUser save(Long uid, HomeUser.LABEL label, String coverImageUrl) throws ActionParameterException, ResourceAlreadyExistedException {
        if (uid == null || label == null || coverImageUrl == null) throw new ActionParameterException("uid");
        if (repository.exists(uid)) throw new ResourceAlreadyExistedException("资源已存在，请勿重复添加");
        HomeUser user = new HomeUser();
        user.setUid(uid);
        user.setCoverImageUrl(coverImageUrl);
        user.setLabel(label);
        user.setDeleteFlag(HomeUser.DELETE.show);
        user.setCreateTime(new Date());
        return repository.save(user);
    }

    private List<HomeUser> transferToHomeUser(List<Object[]> objects) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<HomeUser> homeUsers = new ArrayList<>();
        for (Object[] object : objects) {
            if (object == null) continue;
            homeUsers.add(transferToHomeUser(object));
        }
        return homeUsers;
    }

    private HomeUser transferToHomeUser(Object[] objects) {
        if (objects == null || objects.length != 8 && objects.length != 9) return null;
        HomeUser user = new HomeUser();
        user.setUid(appVOUtils.parseLong(objects[0]));
        user.setDeleteFlag(null);
        user.setNickname(appVOUtils.parseString(objects[1]));
        user.setLikeNum(appVOUtils.parseLong(objects[2]));
        user.setHeadIconUrl(appVOUtils.parseString(objects[3]));
        user.setIdentifications(appVOUtils.parseBytesToLongList(objects[4]));
        user.setCoverImageUrl(appVOUtils.parseString(objects[5]));
        user.setLabel(praseLabel(appVOUtils.parseInt(objects[6])));
        user.setCreateTime(appVOUtils.parseDate(objects[7]));
        if (objects.length == 9)
            user.setLikeIt(1 == appVOUtils.parseInt(objects[8]));
        else
            user.setLikeIt(false);
        return user;
    }

    private HomeUser.LABEL praseLabel(Integer label) {
        if (label == null) return null;
        if (label == 0) return HomeUser.LABEL.youwu;
        if (label == 1) return HomeUser.LABEL.hotperson;
        return null;
    }
}
