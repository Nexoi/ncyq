package com.seeu.ywq.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.seeu.core.R;
import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import com.seeu.ywq.message.model.SysMessage;
import com.seeu.ywq.message.service.SysMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(tags = "系统通知-发布通知", description = "管理员为所有用户发布通知")
@RestController
@RequestMapping("/api/admin/v1/notification")
//@PreAuthorize("hasRole('ADMIN')")
public class NotificationPushApi {

    @Autowired
    private PushService pushService;
    @Autowired
    private SysMessageService sysMessageService;

    @ApiOperation(value = "为所有人发布通知", notes = "字段 extraJson 必须为 JSON Object 格式，如 { key:value }")
    @PostMapping("/sys")
    public ResponseEntity sendPush(String text, String linkUrl, String extraJson) {
        try {
            JSONObject jsonObject = JSON.parseObject(extraJson);

            // 持久化
            SysMessage message = new SysMessage();
            message.setCreateTime(new Date());
            message.setText(text);
            message.setLinkUrl(linkUrl);
            message.setExtraJson(JSON.toJSONString(jsonObject));
            sysMessageService.add(message);
            // 推送
            pushService.sysPush(text, linkUrl, jsonObject);
            return ResponseEntity.ok(R.code(200).message("发布成功！").build());
        } catch (PushException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("发布失败！" + e.getMessage()).build());
        } catch (JSONException e1) {
            return ResponseEntity.badRequest().body(R.code(4002).message("JSON 解析失败！").build());
        }
    }
}
