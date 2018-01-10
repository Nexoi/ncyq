package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.service.VIPTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户VIP", description = "个人VIP信息查看/成为VIP/查看VIP种类")
@RestController
@RequestMapping("/api/v1/user/vip")
public class VIPApi {

    @Autowired
    private VIPTableService vipTableService;

    @ApiOperation(value = "查看VIP种类", notes = "比如：分年卡、月卡、季卡，每种卡对应其价格")
    @GetMapping("/list")
    public ResponseEntity list() {
        return ResponseEntity.ok(vipTableService.findAll());
    }

    @ApiOperation(value = "根据天数获取VIP卡信息", notes = "")
    @GetMapping("/day/{day}")
    public ResponseEntity get(@PathVariable Long day) {
        try {
            return ResponseEntity.ok(vipTableService.findByDay(day));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("无此会员卡可购买"));
        }
    }

    // 购买 VIP 卡
    // TODO

}
