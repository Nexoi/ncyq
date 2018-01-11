package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.uservip.model.VIPTable;
import com.seeu.ywq.uservip.service.VIPTableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户VIP配置", description = "配置VIP充值类型")
@RestController("adminVIPApi")
@RequestMapping("/api/admin/v1/user/vip")
@PreAuthorize("hasRole('ADMIN')")
public class VIPConfigurerApi {
    @Autowired
    private VIPTableService vipTableService;

    @PostMapping("/configurer")
    public ResponseEntity configurerAdd(@Validated VIPTable vip) {
        try {
            vipTableService.findByDay(vip.getDay());
            return ResponseEntity.badRequest().body(R.code(4000).message("该配置已经存在，不可重复添加，如需要更新该配置，可使用 [PUT] 方法操作该接口"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(vipTableService.save(vip));
        }
    }

    @PutMapping("/configurer")
    public ResponseEntity configurerUpdate(@Validated VIPTable vip) {
        try {
            vipTableService.findByDay(vip.getDay());
            return ResponseEntity.ok(vipTableService.save(vip));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("该配置不存在，如需添加，可使用 [POST] 方法操作该接口"));
        }
    }
}
