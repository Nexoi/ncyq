package com.seeu.ywq.api.admin;


import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Api(tags = "配置文件",description = "每日任务/账户交易系统")
@RestController
@RequestMapping("/api/admin/v1/configurer")
@PreAuthorize("hasRole('ADMIN')")
public class ConfigurerApi {

    @Autowired
    private GlobalConfigurerService globalConfigurerService;
    @Autowired
    private TaskConfigurerService taskConfigurerService;

    @ApiOperation(value = "获取账户交易系统配置")
    @GetMapping("/account/list")
    public ResponseEntity accountList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap();
        map.put("用户钻石分成比例", globalConfigurerService.getUserDiamondsPercent());
        map.put("绑定用户分成比例", globalConfigurerService.getBindUserShareDiamondsPercent());
        map.put("微信解锁消耗钻石数", globalConfigurerService.getUnlockWeChat());
        map.put("人民币兑钻石比例", globalConfigurerService.getRMBToDiamondsRatio());
        map.put("钻石兑金币比例", globalConfigurerService.getDiamondToCoinsRatio());
        int i = 1;
        for (String key : map.keySet()) {
            Map<String, Object> vo = new HashMap();
            vo.put("title", key);
            vo.put("key", i++);
            vo.put("value", map.get(key));
            list.add(vo);
        }
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "获取任务配置",notes = "每日任务/长期任务")
    @GetMapping("/task/list")
    public ResponseEntity taskList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap();
        map.put("每日至少点赞数量", taskConfigurerService.getTaskClickLikeProgress());
        map.put("每日至少评论数量", taskConfigurerService.getTaskCommentProgress());
        map.put("每日至少分享数量", taskConfigurerService.getTaskShareProgress());
        map.put("新手大礼包可领取个数", taskConfigurerService.getTaskNewHerePackageNumber());
        int i = 6;
        for (String key : map.keySet()) {
            Map<String, Object> vo = new HashMap();
            vo.put("title", key);
            vo.put("key", i++);
            vo.put("value", map.get(key));
            list.add(vo);
        }
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "修改配置")
    @PutMapping("/configurer/{key}")
    public ResponseEntity configurer(@PathVariable(required = true) Integer key,
                                     @RequestParam(required = true) BigDecimal value) {
        try {
            switch (key) {
                case 1:
                    globalConfigurerService.setUserDiamondsPercent(value.floatValue());
                    return ResponseEntity.ok(R.code(200).message("【用户钻石分成比例】设定成功"));
                case 2:
                    globalConfigurerService.setBindUserShareDiamondsPercent(value.floatValue());
                    return ResponseEntity.ok(R.code(200).message("【绑定用户分成比例】设定成功"));
                case 3:
                    globalConfigurerService.setUnlockWeChat(value.longValue());
                    return ResponseEntity.ok(R.code(200).message("【微信解锁消耗钻石数】设定成功"));
                case 4:
                    globalConfigurerService.setRMBToDiamondsRatio(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【人民币兑钻石比例】设定成功"));
                case 5:
                    globalConfigurerService.setDiamondToCoinsRatio(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【钻石兑金币比例】设定成功"));
                case 6:
                    taskConfigurerService.setTaskClickLikeProgress(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【每日至少点赞数量】设定成功"));
                case 7:
                    taskConfigurerService.setTaskCommentProgress(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【每日至少评论数量】设定成功"));
                case 8:
                    taskConfigurerService.setTaskShareProgress(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【每日至少分享数量】设定成功"));
                case 9:
                    taskConfigurerService.setTaskNewHerePackageNumber(value.intValue());
                    return ResponseEntity.ok(R.code(200).message("【新手大礼包可领取个数】设定成功"));
                default:
                    return ResponseEntity.badRequest().body(R.code(4000).message("传入参数 key 错误"));
            }
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("传入参数 value 错误"));
        }
    }
}
