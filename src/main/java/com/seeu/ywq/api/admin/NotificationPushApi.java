package com.seeu.ywq.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seeu.core.R;
import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "发布系统通知", description = "管理员为所有用户发布通知")
@RestController
@RequestMapping("/api/admin/v1/notification")
@PreAuthorize("hasRole('ADMIN')")
public class NotificationPushApi {

    @Autowired
    private PushService pushService;

    @ApiOperation(value = "为所有人发布通知", notes = "字段 extraJson 必须为 JSON Object 格式，如 { key:value }")
    @PostMapping("/sys")
    public ResponseEntity sendPush(String text, String linkUrl, String extraJson) {
        JSONObject jsonObject = JSON.parseObject(extraJson);
        try {
            pushService.sysPush(text, linkUrl, jsonObject);
            return ResponseEntity.ok(R.code(200).message("发布成功！").build());
        } catch (PushException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("发布失败！" + e.getMessage()).build());
        }
    }
}
