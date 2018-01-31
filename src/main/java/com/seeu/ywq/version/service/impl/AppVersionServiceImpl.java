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
    public boolean shouldUpdate(Integer clientVersion, AppVersion.CLIENT client) {
        AppVersion appVersion = repository.findTop1ByClientAndUpdateOrderByVersionDesc(client, AppVersion.FORCE_UPDATE.FORCE);
        if (appVersion == null) return false;
        if (appVersion.getVersion() > clientVersion) return true;
        return false;
    }

    @Override
    public boolean hasUpdate(Integer clientVersion, AppVersion.CLIENT client) {
        AppVersion appVersion = repository.findTop1ByClientOrderByVersionDesc(client);
        if (appVersion == null) return false;
        if (appVersion.getVersion() > clientVersion) return true;
        return false;
    }

    @Override
    public List<AppVersion> findAllAvailable(Integer clientVersion, AppVersion.CLIENT client) {
        if (clientVersion == null) clientVersion = 0;
        return repository.findAllByClientAndVersionGreaterThanOrderByVersionDesc(client, clientVersion);
    }

    @Override
    public AppVersion getNewVersion(AppVersion.CLIENT client) {
        return repository.findTop1ByClientOrderByVersionDesc(client);
    }

    @Override
    public AppVersion getNewForceVersion(AppVersion.CLIENT client) {
        return repository.findTop1ByClientAndUpdateOrderByVersionDesc(client, AppVersion.FORCE_UPDATE.FORCE);
    }

    @Override
    public Page<AppVersion> findAll(AppVersion.CLIENT client, Pageable pageable) {
        return repository.findAllByClient(client, pageable);
    }

    @Override
    public AppVersion save(AppVersion appVersion) {
        if (appVersion == null) return null;
        appVersion.setUpdateTime(new Date());
        return repository.save(appVersion);
    }
}
