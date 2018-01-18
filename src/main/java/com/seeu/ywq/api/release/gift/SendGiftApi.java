package com.seeu.ywq.api.release.gift;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.AmountCannotBeNegetiveException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.service.GiftService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "打赏送礼（寄送）", description = "寄送礼物到家门")
@RestController
@RequestMapping("/api/v1/gift")
public class SendGiftApi {

    @Autowired
    private GiftService giftService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "列出所有可送礼物")
    @GetMapping("/list")
    public ResponseEntity listAll(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(giftService.findAll(new PageRequest(page, size)));
    }


    @ApiOperation("送礼")
    @PostMapping("/{uid}")
    public ResponseEntity createSend(@AuthenticationPrincipal UserLogin authUser,
                                     @PathVariable Long uid,
                                     @RequestParam Long rewardResourceId,
                                     @RequestParam Integer amount) {
        try {
            GiftOrder order = orderService.createSendGift(authUser.getUid(), uid, rewardResourceId, amount);
            return ResponseEntity.ok(order);
        } catch (BalanceNotEnoughException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("余额不足").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("找不到该物品可以赠送").build());
        } catch (AmountCannotBeNegetiveException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("赠送数量只能为正整数").build());
        } catch (ActionNotSupportException e) {
            return ResponseEntity.badRequest().body(R.code(4003).message("资源设定异常，请联系管理员解决").build());
        }
    }
}
