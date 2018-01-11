package com.seeu.ywq.api.release.task;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.NewHerePackageReceiveEmptyException;
import com.seeu.ywq.exception.SignInTodayAlreadyFinishedException;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.service.DayFlushTaskService;
import com.seeu.ywq.task.service.StaticTaskService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = {"任务"}, description = "查看每日任务/新人礼包/每日签到", position = 10)
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskApi {
    @Autowired
    private DayFlushTaskService dayFlushTaskService;
    @Autowired
    private StaticTaskService staticTaskService;

    @ApiOperation(value = "获取今日任务")
    @GetMapping("/today")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listDayFlush(@AuthenticationPrincipal UserLogin authUser) {
        return ResponseEntity.ok(dayFlushTaskService.list(authUser.getUid(), new Date()));
    }


    @ApiOperation(value = "获取每日任务(按所给日期)")
    @GetMapping("/byday/{day}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listStatic(@AuthenticationPrincipal UserLogin authUser,
                                     @ApiParam(value = "20180120")
                                     @PathVariable Long day) {
        if (String.valueOf(day).length() != 8)
            return ResponseEntity.badRequest().body(R.code(400).message("传入参数 [day] 必须为 8 位纯数字"));
        return ResponseEntity.ok(dayFlushTaskService.list(authUser.getUid(), day));
    }

    @ApiOperation(value = "获取长期任务", notes = "新人礼包等")
    @GetMapping("/longtime")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listLongTime(@AuthenticationPrincipal UserLogin authUser) {
        return ResponseEntity.ok(staticTaskService.list(authUser.getUid()));
    }


    @ApiOperation(value = "新人礼包领取")
    @PostMapping("/gift")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity xinrenlibao(@AuthenticationPrincipal UserLogin authUser) {
        try {
            return ResponseEntity.ok(staticTaskService.receiveNewHerePackage(authUser.getUid()));
        } catch (NewHerePackageReceiveEmptyException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("您已经领取过新人礼物了"));
        }
    }

    @ApiOperation(value = "每日签到")
    @PostMapping("/sign")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity signinToday(@AuthenticationPrincipal UserLogin authUser) {
        try {
            return ResponseEntity.ok(dayFlushTaskService.signToday(authUser.getUid()));
        } catch (SignInTodayAlreadyFinishedException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("您今天已经签过到了"));
        }
    }
}
