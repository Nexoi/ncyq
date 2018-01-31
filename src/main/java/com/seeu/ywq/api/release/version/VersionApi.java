package com.seeu.ywq.api.release.version;

import com.seeu.core.R;
import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 5:09 PM
 * Describe:
 */
@Api(tags = "版本更新", description = "客户端更新")
@RestController
@RequestMapping("/api/v1/version")
public class VersionApi {

    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation("检查最新版本")
    @GetMapping("/{client}/new")
    public ResponseEntity get(@PathVariable AppVersion.CLIENT client) {
        AppVersion appVersion = appVersionService.getNewVersion(client);
        if (appVersion == null) return ResponseEntity.status(204).body(R.code(204).message("无可用更新"));
        return ResponseEntity.ok(appVersion);
    }

    @ApiOperation("检查最新强制更新版本")
    @GetMapping("/{client}/new/force")
    public ResponseEntity getForce(@PathVariable AppVersion.CLIENT client) {
        AppVersion appVersion = appVersionService.getNewForceVersion(client);
        if (appVersion == null) return ResponseEntity.status(204).body(R.code(204).message("无可用更新"));
        return ResponseEntity.ok(appVersion);
    }

    @ApiOperation("检查可用的更新版本")
    @GetMapping("/{client}/available")
    public List<AppVersion> listAll(@PathVariable AppVersion.CLIENT client,
                                    @RequestParam Integer version) {
        return appVersionService.findAllAvailable(version, client);
    }

}
