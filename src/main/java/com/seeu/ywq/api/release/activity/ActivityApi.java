package com.seeu.ywq.api.release.activity;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.ywqactivity.model.Activity;
import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import com.seeu.ywq.ywqactivity.service.ActivityCheckInService;
import com.seeu.ywq.ywqactivity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = "活动页面", description = "活动列表")
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityApi {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityCheckInService activityCheckInService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity list(@RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
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

//    // 报名接口
//    @PostMapping("/checkIn/{activityId}")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity checkIn(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long activityId) {
//        Activity activity = activityService.findOne(activityId);
//        if (activity == null)
//            return ResponseEntity.status(404).body(R.code(404).message("找不到该活动信息"));
//        // 报名注册
//        ActivityCheckIn checkIn = new ActivityCheckIn();
//        checkIn.setUid(authUser.getUid());
//        checkIn.setActivityId(activityId);
//        checkIn.setHasPaid(false);
//        checkIn.setUpdateTime(new Date());
//        try {
//            ActivityCheckIn checkIn1 = activityCheckInService.save(checkIn);
//            return ResponseEntity.status(201).body(checkIn1);
//        } catch (ResourceAlreadyExistedException e) {
//            return ResponseEntity.status(400).body(R.code(400).message("您已经报过名了"));
//        }
//    }

    @ApiOperation("不需要下单，直接支付")
    @PostMapping("/check-in/{activityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity pay(@AuthenticationPrincipal UserLogin authUser,
                              HttpServletRequest request,
                              @RequestParam TradeModel.PAYMENT payment,
                              @RequestParam Integer quantity,
                              @PathVariable Long activityId) {
//        ActivityCheckIn checkInModel = activityCheckInService.findOne(checkInId);
//        if (checkInModel == null)
//            return ResponseEntity.status(400).body(R.code(400).message("您还未报名，不可进行支付"));
        try {
            return ResponseEntity.ok(orderService.createActivity(authUser.getUid(), activityId, quantity, payment, request));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message(e.getMessage()));
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message(e.getMessage()));
        } catch (ActionParameterException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message(e.getMessage()));
        }
    }

}
