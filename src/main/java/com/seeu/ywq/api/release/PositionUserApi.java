package com.seeu.ywq.api.release;

import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Api(tags = "附近的人", description = "获取附近的人信息")
@RestController
@RequestMapping("/api/v1/nearby")
public class PositionUserApi {
    @Autowired
    private UserPositionService userPositionService;

    @ApiOperation(value = "获取附近的人信息", notes = "（注：如果是已登陆用户，会将该用户的位置设定为传入的经纬度信息）需要传入：性别（可选，不填则表示所有），当前经纬度信息，测量距离，分页信息。分页默认：第 0 页，10 条。返回值中 distance 单位为：千米")
    @GetMapping
    public ResponseEntity getNearBy(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(required = false) UserLogin.GENDER gender,
                                    BigDecimal longitude,
                                    BigDecimal latitude,
                                    @RequestParam(defaultValue = "5") Long distance,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        Long uid = 0L;
        if (authUser != null) {
            userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
            uid = authUser.getUid();
        }
        if (gender != null) {
            return ResponseEntity.ok(userPositionService.findNear(uid, gender, distance, longitude, latitude, new PageRequest(page, size)));
        }
        return ResponseEntity.ok(userPositionService.findNear(uid, distance, longitude, latitude, new PageRequest(page, size)));
    }
}
