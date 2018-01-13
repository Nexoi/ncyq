package com.seeu.ywq.api.release.activity;

import com.seeu.core.R;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.ywqactivity.model.Activity;
import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import com.seeu.ywq.ywqactivity.service.ActivityCheckInService;
import com.seeu.ywq.ywqactivity.service.ActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "活动页面", description = "活动列表")
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityApi {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityCheckInService activityCheckInService;

    @GetMapping("/list")
    public ResponseEntity list(@RequestParam Integer page, @RequestParam Integer size) {
        Page<Activity> activities = activityService.findAll(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "updateTime")));
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Activity activity = activityService.findOne(id);
        if (activity == null)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
        return ResponseEntity.ok(activity);
    }

    // 报名接口
    @PostMapping("/checkIn/{activityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity checkIn(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long activityId) {
        Activity activity = activityService.findOne(activityId);
        if (activity == null)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
        // 报名注册
        ActivityCheckIn checkIn = new ActivityCheckIn();
        checkIn.setUid(authUser.getUid());
        checkIn.setActivityId(activityId);
        checkIn.setHasPaid(false);
        checkIn.setUpdateTime(new Date());
        return ResponseEntity.status(201).body(R.code(201).message("报名成功！请尽快完成支付"));
    }

}
