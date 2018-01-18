package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.photography.model.PhotographyDay;
import com.seeu.ywq.photography.service.PhotographyDayService;
import com.seeu.ywq.photography.service.PhotographyService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "下载相册（摄影集）", description = "管理员通过接口进行上传照片，用户在此下载")
@RestController
@RequestMapping("/api/v1/photographs")
public class PhotographyApi {
    @Autowired
    private PhotographyService photographyService;
    @Autowired
    private PhotographyDayService photographyDayService;

    @ApiOperation(value = "查看自己照片下载页面【分页】", notes = "管理员通过接口进行上传照片，用户可以在此下载")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listDownloads(@AuthenticationPrincipal UserLogin authUser,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        PhotographyDay day = photographyDayService.findByUid(authUser.getUid());
        Page photoPage = photographyService.findAllByUid(authUser.getUid(), new PageRequest(page, size));
        if (day == null || day.getTerminateTime() == null || photoPage.getTotalElements() == 0) {
//            map.put("terminateTime", "unset");
            return ResponseEntity.status(204).body(R.code(204).message("无数据"));
        } else {
            // set info
            Map map = new HashMap();
            map.put("terminateTime", day.getTerminateTime());
            map.put("terminateDay", Long.valueOf((day.getTerminateTime().getTime() - new Date().getTime()) / 86400000));
            map.put("photographs", photoPage);
            return ResponseEntity.ok(map);
        }
    }
}
