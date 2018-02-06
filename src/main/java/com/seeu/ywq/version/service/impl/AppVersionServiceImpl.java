package com.seeu.ywq.version.service.impl;

import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.repository.AppVersionRepository;
import com.seeu.ywq.version.service.AppVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 5:02 PM
 * Describe:
 */

@Service
public class AppVersionServiceImpl implements AppVersionService {
    @Resource
    private AppVersionRepository repository;

    @Override
    public boolean shouldUpdate(Integer clientVersion) {
        AppVersion appVersion = repository.findTop1ByUpdateOrderByVersionDesc(AppVersion.FORCE_UPDATE.FORCE);
        if (appVersion == null) return false;
        if (appVersion.getVersion() > clientVersion) return true;
        return false;
    }

    @Override
    public boolean hasUpdate(Integer clientVersion) {
        AppVersion appVersion = repository.findTop1ByOrderByVersionDesc();
        if (appVersion == null) return false;
        if (appVersion.getVersion() > clientVersion) return true;
        return false;
    }

    @Override
    public List<AppVersion> findAllAvailable(Integer clientVersion) {
        if (clientVersion == null) clientVersion = 0;
        return repository.findAllByVersionGreaterThanOrderByVersionDesc(clientVersion);
    }

    @Override
    public AppVersion getNewVersion() {
        return repository.findTop1ByOrderByVersionDesc();
    }

    @Override
    public AppVersion getNewForceVersion() {
        return repository.findTop1ByUpdateOrderByVersionDesc(AppVersion.FORCE_UPDATE.FORCE);
    }

    @Override
    public Page<AppVersion> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public AppVersion save(AppVersion appVersion) {
        if (appVersion == null) return null;
        appVersion.setUpdateTime(new Date());
        return repository.save(appVersion);
    }

    @Override
    public void delete(Integer id) {
        AppVersion version = repository.findOne(id);
        if (version != null)
            repository.delete(version);
    }
}
