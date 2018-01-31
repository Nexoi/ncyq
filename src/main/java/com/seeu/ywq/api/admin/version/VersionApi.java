package com.seeu.ywq.api.admin.version;

import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 5:51 PM
 * Describe:
 */


@Api(tags = "版本更新", description = "APP 版本信息")
@RestController("adminAppVersionApi")
@RequestMapping("/api/admin/v1/version")
public class VersionApi {

    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation("列出所有版本")
    @GetMapping("/{client}/list")
    public Page<AppVersion> list(@PathVariable AppVersion.CLIENT client,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return appVersionService.findAll(client, new PageRequest(page, size));
    }

    @ApiOperation("添加／更新一个版本")
    @PostMapping("/{client}")
    public AppVersion update(@PathVariable AppVersion.CLIENT client, AppVersion appVersion) {
        appVersion.setClient(client);
        return appVersionService.save(appVersion);
    }
}
