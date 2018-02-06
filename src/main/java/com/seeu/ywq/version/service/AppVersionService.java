package com.seeu.ywq.version.service;

import com.seeu.ywq.version.model.AppVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 4:58 PM
 * Describe:
 */

public interface AppVersionService {

    // 是否需要更新（FORCE更新）
    boolean shouldUpdate(Integer clientVersion);

    // 是否有更新
    boolean hasUpdate(Integer clientVersion);

    List<AppVersion> findAllAvailable(Integer clientVersion);

    AppVersion getNewVersion();

    AppVersion getNewForceVersion(); // 这是必要的更新

    // admin //

    Page<AppVersion> findAll(Pageable pageable);

    AppVersion save(AppVersion appVersion);

    void delete(Integer id);
}
