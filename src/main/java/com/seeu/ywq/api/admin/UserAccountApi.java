package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.pay.model.RechargeTable;
import com.seeu.ywq.pay.service.RechargeTableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "充值配置", description = "配置充值类型")
@RestController("adminUserAccountApi")
@RequestMapping("/api/admin/v1/account")
//@PreAuthorize("hasRole('ADMIN')")
public class UserAccountApi {

    @Autowired
    private RechargeTableService rechargeTableService;


    @GetMapping("/recharge/list")
    public List<RechargeTable> list() {
        return rechargeTableService.findAll();
    }

    @GetMapping("/recharge")
    public ResponseEntity get(@RequestParam Double price) {
        RechargeTable table = rechargeTableService.findOne(BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_UP));
        if (table == null)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该配置"));
        return ResponseEntity.ok(table);
    }

    @PostMapping("/recharge")
    public ResponseEntity add(@Validated RechargeTable table) {
        BigDecimal price = table.getPrice();
        if (null != rechargeTableService.findOne(price))
            return ResponseEntity.status(400).body(R.code(400).message("该配置已经存在，若需要修改该配置可以使用 [PUT] 方法操作该接口"));
        return ResponseEntity.ok(rechargeTableService.save(table));
    }

    @PutMapping("/recharge")
    public ResponseEntity update(@Validated RechargeTable table) {
        BigDecimal price = table.getPrice();
        if (null == rechargeTableService.findOne(price))
            return ResponseEntity.status(404).body(R.code(404).message("找不到该配置，若需要新建配置可以使用 [POST] 方法操作该接口"));
        return ResponseEntity.ok(rechargeTableService.save(table));
    }
}
