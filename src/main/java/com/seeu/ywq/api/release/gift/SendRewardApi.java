package com.seeu.ywq.api.release.gift;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.RewardAmountCannotBeNegitiveException;
import com.seeu.ywq.exception.RewardResourceNotFoundException;
import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.service.RewardService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "打赏（不寄送）", description = "礼品：花")
@RestController
@RequestMapping("/api/v1/reward")
public class SendRewardApi {

    @Autowired
    private RewardService rewardService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "获取打赏物品列表")
    @GetMapping("/list")
    public ResponseEntity listFlowers(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(rewardService.findAll(new PageRequest(page, size)));
    }

    @ApiOperation("打赏给某人")
    @PostMapping("/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity rewardHer(@AuthenticationPrincipal UserLogin authUser,
                                    @PathVariable Long uid,
                                    @RequestParam Long rewardResourceId,
                                    @RequestParam Integer amount) {
        try {
            GiftOrder log = orderService.createReward(authUser.getUid(), uid, rewardResourceId, amount);
            return ResponseEntity.ok(log);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("余额不足").build());
        } catch (RewardResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("找不到该物品可以赠送").build());
        } catch (RewardAmountCannotBeNegitiveException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("赠送数量只能为正整数").build());
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("资源设定异常，请联系管理员解决").build());
        }
    }
}
