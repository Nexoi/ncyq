package com.seeu.ywq.version.repository;

import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.model.AppVersionPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 4:57 PM
 * Describe:
 */

public interface AppVersionRepository extends JpaRepository<AppVersion, AppVersionPKeys> {

    AppVersion findTop1ByClientOrderByVersionDesc(@Param("client") AppVersion.CLIENT client);

    AppVersion findTop1ByClientAndUpdateOrderByVersionDesc(@Param("client") AppVersion.CLIENT client, @Param("update") AppVersion.FORCE_UPDATE update);

    List<AppVersion> findAllByClientAndVersionGreaterThanOrderByVersionDesc(@Param("client") AppVersion.CLIENT client, @Param("version") Integer version);

    Page<AppVersion> findAllByClient(@Param("client") AppVersion.CLIENT client, Pageable pageable);
}
