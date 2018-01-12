package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户位置更新", description = "查看/更新自己当前设定的位置")
@RestController
@RequestMapping("/api/v1/position")
public class PositionApi {

    @Autowired
    private UserPositionService userPositionService;


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser) {
        BigDecimal[] position = userPositionService.findMyPosition(authUser.getUid());
        if (position == null || position.length != 2)
            return ResponseEntity.badRequest().body(R.code(400).message("您还未设定位置信息，请用 [PUT] 方法调用该接口更新信息"));
        Map map = new HashMap();
        map.put("longitude", position[0]);
        map.put("latitude", position[1]);
        return ResponseEntity.ok(map);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity update(@AuthenticationPrincipal UserLogin authUser,
                                 BigDecimal longitude,
                                 BigDecimal latitude) {
        userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
        return ResponseEntity.ok(R.code(200).message("更新成功"));
    }
}
