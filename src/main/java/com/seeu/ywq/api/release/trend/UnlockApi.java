package com.seeu.ywq.api.release.trend;


import com.seeu.core.R;
import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.globalconfig.exception.GlobalConfigSettingException;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.userlogin.exception.PhoneNumberNetSetException;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "解锁资源", description = "需要付费的操作")
@RestController
@RequestMapping("/api/v1/unlock")
public class UnlockApi {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GlobalConfigurerService globalConfigurerService;
    @Autowired
    private PublishService publishService;

    @ApiOperation(value = "解锁某一条首页视频", notes = "根据发布视频的ID解锁动态【按天收取费用】，激活成功会返回支付订单记录信息")
    @PostMapping("/video/{videoId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockHomePageVideo(@AuthenticationPrincipal UserLogin authUser,
                                              @PathVariable("videoId") Long videoId) {
        try {
            OrderLog log = orderService.createUnlockHomePageVideo(authUser.getUid(), videoId);
            return ResponseEntity.ok(log);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足").build());
        } catch (ResourceAlreadyActivedException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("该资源已经被解锁，无需重复解锁").build());
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("该资源设定解锁价格异常，无法解锁").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该视频资源").build());
        }
    }

    @ApiOperation(value = "解锁某一条动态", notes = "根据发布动态ID解锁动态【按天收取费用】，激活成功会返回支付订单记录信息")
    @PostMapping("/publish/{publishId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockPublish(@AuthenticationPrincipal UserLogin authUser,
                                        @PathVariable("publishId") Long publishId) {
        try {
            OrderLog log = orderService.createUnlockPublish(authUser.getUid(), publishId);
            return ResponseEntity.ok(log);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足").build());
        } catch (PublishNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build());
        } catch (ResourceAlreadyActivedException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("该资源已经被解锁，无需重复解锁").build());
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("该资源设定解锁价格异常，无法解锁").build());
        }
    }

    @ApiOperation(value = "获取解锁一条动态的价格（钻石）")
    @GetMapping("/publish/{publishId}/diamonds")
    public ResponseEntity getUnlockPublishDiamonds(@PathVariable Long publishId) {
        try {
            Long diamonds = publishService.getUnlockDiamonds(publishId);
            Map map = new HashMap();
            map.put("diamonds", diamonds);
            return ResponseEntity.ok(map);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该动态"));
        } catch (PublishTYPENotAllowedException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("该动态类型不需要解锁"));
        }
    }

    @ApiOperation(value = "获取用户微信", notes = "短信发送给用户微信号码【按次收取费用】")
    @PostMapping("/wechat/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockWechat(@AuthenticationPrincipal UserLogin authUser,
                                       @PathVariable("uid") Long uid) {
        try {
            OrderLog log = orderService.createUnlockWeChatID(authUser.getUid(), uid);
            return ResponseEntity.ok(log);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足").build());
        } catch (WeChatNotSetException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("该用户未设定微信号码").build());
        } catch (SMSSendFailureException e) {
            return ResponseEntity.badRequest().body(R.code(4004).message("短信发送失败，请稍后再试").build());
        } catch (GlobalConfigSettingException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("系统设定异常，请联系管理员解决").build());
        } catch (ActionParameterException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("该操作不可对自己进行").build());
        }
    }

    @ApiOperation(value = "获取解锁一次微信的价格（钻石）")
    @GetMapping("/wechat/{uid}/diamonds")
    public ResponseEntity getUnlockWeChatDiamonds(@PathVariable Long uid) {
        Long diamonds = globalConfigurerService.getUnlockWeChat();
        if (diamonds == null) {
            // TODO 设定默认值为 66L ？
            // diamonds = 0L;
            return ResponseEntity.badRequest().body(R.code(400).message("管理员未设定解锁价格，请联系管理员解决"));
        }
        Map map = new HashMap();
        map.put("diamonds", diamonds);
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "获取用户手机号", notes = "若成功，将直接返回该用户的手机")
    @PostMapping("/phone/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity unlockPhone(@AuthenticationPrincipal UserLogin authUser,
                                      @PathVariable("uid") Long uid) {
        try {
            String phoneNumber = orderService.createUnlockPhoneNumber(authUser.getUid(), uid);
            Map map = new HashMap();
            map.put("uid", uid);
            map.put("phone", phoneNumber);
            return ResponseEntity.ok(map);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("余额不足").build());
        } catch (PhoneNumberNetSetException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("该用户未设定手机号码").build());
        } catch (GlobalConfigSettingException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("系统设定异常，请联系管理员解决").build());
        } catch (ActionParameterException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("该操作不可对自己进行").build());
        }
    }

    @ApiOperation(value = "获取解锁一次手机的价格（钻石）")
    @GetMapping("/phone/{uid}/diamonds")
    public ResponseEntity getUnlockPhoneDiamonds(@PathVariable Long uid) {
        Long diamonds = globalConfigurerService.getUnlockPhone();
        if (diamonds == null) {
            // TODO 设定默认值为 66L ？
            // diamonds = 0L;
            return ResponseEntity.badRequest().body(R.code(400).message("管理员未设定解锁价格，请联系管理员解决"));
        }
        Map map = new HashMap();
        map.put("diamonds", diamonds);
        return ResponseEntity.ok(map);
    }
}
