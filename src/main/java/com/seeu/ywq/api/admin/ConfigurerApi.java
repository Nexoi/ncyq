package com.seeu.ywq.api.admin;


import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.service.TaskCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Api(tags = "配置文件", description = "每日任务/账户交易系统")
@RestController
@RequestMapping("/api/admin/v1/configurer")
//@PreAuthorize("hasRole('ADMIN')")
public class ConfigurerApi {

    @Autowired
    private GlobalConfigurerService globalConfigurerService;
    @Autowired
    private TaskCategoryService taskCategoryService;

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


    @ApiOperation(value = "修改配置")
    @PutMapping("/account/{key}")
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
//                case 6:
//                    taskConfigurerService.setTaskClickLikeProgress(value.intValue());
//                    return ResponseEntity.ok(R.code(200).message("【每日至少点赞数量】设定成功"));
//                case 7:
//                    taskConfigurerService.setTaskCommentProgress(value.intValue());
//                    return ResponseEntity.ok(R.code(200).message("【每日至少评论数量】设定成功"));
//                case 8:
//                    taskConfigurerService.setTaskShareProgress(value.intValue());
//                    return ResponseEntity.ok(R.code(200).message("【每日至少分享数量】设定成功"));
//                case 9:
//                    taskConfigurerService.setTaskNewHerePackageNumber(value.intValue());
//                    return ResponseEntity.ok(R.code(200).message("【新手大礼包可领取个数】设定成功"));
                default:
                    return ResponseEntity.badRequest().body(R.code(4000).message("传入参数 key 错误"));
            }
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("传入参数 value 错误"));
        }
    }

    @ApiOperation(value = "获取任务配置", notes = "每日任务/长期任务")
    @GetMapping("/task/list")
    public ResponseEntity taskList() {
        List<TaskCategory> categories = taskCategoryService.findAll();
        // check
        if (categories.size() == 5) return ResponseEntity.ok(categories);
        Map<TaskCategory.CATEGORY, TaskCategory> map = new HashMap();
        for (TaskCategory category : categories) {
            map.put(category.getCategory(), category);
        }
        // 检查 5 个是否正常
        for (TaskCategory.CATEGORY category : TaskCategory.CATEGORY.values()) {
            if (map.get(category) != null) continue;
            TaskCategory taskCategory = new TaskCategory();
            taskCategory.setCategory(category);
            taskCategory.setCoin(0L);
            taskCategory.setTotalProgress(0);
            taskCategory.setUpdateTime(new Date());
            taskCategory.setTitle("");
            taskCategory.setSubTitle("");
            taskCategory.setType(category.isDayFlush() ? TaskCategory.TYPE.DAYFLUSH : TaskCategory.TYPE.LONGTIME);
            categories.add(taskCategory);
        }
        return ResponseEntity.ok(taskCategoryService.save(categories));
    }

    @ApiOperation(value = "配置每日任务")
    @PutMapping("/task/{category}")
    public ResponseEntity configurerTask(@PathVariable TaskCategory.CATEGORY category,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) String subTitle,
                                         @RequestParam(required = false) Long coin,
                                         @RequestParam(required = false) Integer totalProgress) {
        TaskCategory taskCategory = taskCategoryService.findOne(category);
        if (taskCategory == null) {
            taskCategory = new TaskCategory();
            taskCategory.setCategory(category);
            taskCategory.setType(category.isDayFlush() ? TaskCategory.TYPE.DAYFLUSH : TaskCategory.TYPE.LONGTIME);
        }
        if (title != null) taskCategory.setTitle(title);
        if (subTitle != null) taskCategory.setSubTitle(subTitle);
        if (coin != null) taskCategory.setCoin(coin);
        if (totalProgress != null) taskCategory.setTotalProgress(totalProgress);
        // check
        if (coin != null && coin < 0)
            return ResponseEntity.badRequest().body(R.code(4000).message("奖励金额不可设定为负值"));
        if (totalProgress != null && totalProgress < 0)
            return ResponseEntity.badRequest().body(R.code(4001).message("任务总完成数量不可设定为负值"));
        return ResponseEntity.ok(taskCategoryService.save(taskCategory));
    }
}
