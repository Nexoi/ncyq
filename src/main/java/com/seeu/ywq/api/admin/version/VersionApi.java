package com.seeu.ywq.api.admin.version;

import com.seeu.core.R;
import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.version.model.AppVersion;
import com.seeu.ywq.version.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

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
@PreAuthorize("hasRole('ADMIN')")
public class VersionApi {

    @Autowired
    private AppVersionService appVersionService;
    @Autowired
    private FileUploadService fileUploadService;
    @Value("${ywq.ios_download_url}")
    private String iosUrl;

    @ApiOperation("列出所有版本")
    @GetMapping("/list")
    public Page<AppVersion> list(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return appVersionService.findAll(new PageRequest(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        appVersionService.delete(id);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }


//    @ApiOperation("添加新IOS版本")
//    @PostMapping("/ios")
//    public AppVersion update(@Validated AppVersion appVersion) {
//        appVersion.setClient(AppVersion.CLIENT.IOS);
//        appVersion.setDownloadUrl(iosUrl);
//        return appVersionService.save(appVersion);
//    }

    @ApiOperation("添加新APK资源")
    @PostMapping("/android")
    public ResponseEntity addNew(MultipartFile apk,
                                 AppVersion.FORCE_UPDATE forceUpdate,
                                 String versionName,
                                 String versionLog) {
        try {
            String url = fileUploadService.uploadAPK(apk);
            AppVersion appVersion = new AppVersion();
//            appVersion.setClient(AppVersion.CLIENT.ANDROID);
            appVersion.setUpdateTime(new Date());
            appVersion.setUpdate(forceUpdate);
            appVersion.setDownloadUrl(url);
            appVersion.setVersionName(versionName);
            appVersion.setUpdateLog(versionLog);
            return ResponseEntity.ok(appVersionService.save(appVersion));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("服务器异常，文件上传失败"));
        }
    }
}
