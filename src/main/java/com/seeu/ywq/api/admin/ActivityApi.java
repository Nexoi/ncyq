package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.ywqactivity.model.Activity;
import com.seeu.ywq.ywqactivity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "活动内容", description = "推荐/活动页面")
@RestController("adminActivityApi")
@RequestMapping("/api/admin/v1/app/activity")
public class ActivityApi {

    @Autowired
    private ActivityService activityService;


    @ApiOperation("查找某一条活动信息")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Activity activity = activityService.findOne(id);
        if (activity == null) return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
        return ResponseEntity.ok(activity);
    }

    @ApiOperation("列出所有的活动信息")
    @GetMapping("/list")
    public ResponseEntity list(@RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(activityService.findAll(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "updateTime"))));
    }

    @ApiOperation("添加活动")
    @PostMapping
    public ResponseEntity add(Activity activity) {
        activity.setDeleteFlag(Activity.DELETE_FLAG.show);
        return ResponseEntity.ok(activityService.save(activity));
    }

    @ApiOperation(value = "更新（覆盖性）", notes = "该更新操作会覆盖原有记录，请务必传输每一个字段")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, Activity activity) {
        Activity activityExisted = activityService.findOne(id);
        if (activityExisted == null) return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
        activity.setId(id);
        return ResponseEntity.ok(activityService.save(activity));
    }

    @ApiOperation("删除活动信息")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            activityService.delete(id);
            return ResponseEntity.ok(R.code(200).message("删除成功！"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
        }
    }

}
