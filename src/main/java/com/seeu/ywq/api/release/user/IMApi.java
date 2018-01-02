package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.third.exception.IMTokenGetException;
import com.seeu.third.im.IMService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "IM 服务", description = "获取会话 Token")
@RestController
@RequestMapping("/api/v1/im")
public class IMApi {
    @Autowired
    private IMService imService;

    @ApiOperation(value = "获取 IM 服务 TOKEN", notes = "此处采用融云")
    @GetMapping("/token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getToken(@AuthenticationPrincipal UserLogin authUser) {
        try {
            String token = imService.getToken(authUser.getUid(), authUser.getNickname(), authUser.getHeadIconUrl());
            Map map = new HashMap();
            map.put("token", token);
            map.put("timestamp", new Date());
            map.put("token_interval", "永久有效");
            return ResponseEntity.ok(map);
        } catch (IMTokenGetException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(R.code(400).message(e.getMessage()));
        }
    }
}
