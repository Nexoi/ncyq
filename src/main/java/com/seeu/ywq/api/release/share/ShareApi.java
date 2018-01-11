package com.seeu.ywq.api.release.share;


import com.seeu.ywq.event_listener.publish_react.ShareEvent;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "分享", description = "分享有礼")
@RestController
@RequestMapping("/api/v1/share")
public class ShareApi {
    @Value("${ywq.share.url.host}")
    private String shareUrlHost;
    @Autowired
    private ApplicationContext applicationContext;

    @ApiOperation(value = "获取分享链接", notes = "该链接针对不同用户生成不同URL，在对应URL页面内进行用户注册可生效“邀请用户”机制")
    @GetMapping("/url")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMine(@AuthenticationPrincipal UserLogin authUser) {
        Long uid = authUser.getUid();
        Map map = new HashMap();
        map.put("url", shareUrlHost + "/signin?author=" + uid);
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "分享成功后调用", notes = "当用户点击分享后，回调此接口完成用户分享任务+1作用")
    @PostMapping("/success")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity shareSuccess(@AuthenticationPrincipal UserLogin authUser) {
        applicationContext.publishEvent(new ShareEvent(this, authUser.getUid(), "", 0L));
        return ResponseEntity.ok().build();
    }
}
