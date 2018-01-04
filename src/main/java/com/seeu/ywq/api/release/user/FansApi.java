package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.user.service.FansService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户粉丝/关注/喜欢", description = "粉丝关注列表查看，关注/取消关注用户，喜欢用户", position = 5)
@RestController
@RequestMapping("/api/v1")
public class FansApi {

    @Autowired
    private FansService fansService;
    @Autowired
    private UserReactService userReactService;

    @ApiOperation(value = "查看自己的粉丝列表【分页】")
    @GetMapping("/fans")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getAllMyFans(@AuthenticationPrincipal UserLogin authUser,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(fansService.findPageByFollowedUid(authUser.getUid(), new PageRequest(page, size)));
    }

    @ApiOperation(value = "查看自己的关注列表【分页】")
    @GetMapping("/follows")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getAllMyFollows(@AuthenticationPrincipal UserLogin authUser,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(fansService.findPageByFansUid(authUser.getUid(), new PageRequest(page, size)));
    }

    @ApiOperation(value = "关注某人")
    @PostMapping("/follow/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity fansSomeOne(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long uid) {
        FansService.STATUS status = fansService.followSomeone(authUser.getUid(), uid);
        switch (status) {
            case success:
                return ResponseEntity.ok().body(R.code(200).message("关注成功！").build());
            case have_followed:
                return ResponseEntity.badRequest().body(R.code(400).message("您已经关注过该用户，无需重复关注").build());
            case no_such_person:
                return ResponseEntity.status(404).body(R.code(404).message("无此用户").build());
            case failure:
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }
    @ApiOperation(value = "取消关注某人")
    @DeleteMapping("/follow/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity cancelFansSomeOne(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long uid) {
        FansService.STATUS status = fansService.cancelFollowSomeone(authUser.getUid(), uid);
        switch (status) {
            case success:
                return ResponseEntity.ok().body(R.code(200).message("取消关注成功！").build());
            case not_followed:
                return ResponseEntity.badRequest().body(R.code(400).message("您还未关注该用户，无法进行该操作").build());
            case no_such_person:
                return ResponseEntity.status(404).body(R.code(404).message("无此用户").build());
            case failure:
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }
    @ApiOperation(value = "喜欢某人", notes = "会让该用户字段：likeNum（喜欢人数）+1")
    @PostMapping("/like/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity likeSomeone(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long uid) {
        UserReactService.STATUS status = userReactService.likeMe(authUser.getUid(), uid);
        switch (status) {
            case contradiction:
                return ResponseEntity.badRequest().body(R.code(4000).message("不可以对自己操作").build());
            case success:
                return ResponseEntity.ok().body(R.code(200).message("喜欢成功，该用户被喜欢次数 +1").build());
            case exist:
                return ResponseEntity.badRequest().body(R.code(4001).message("您已经喜欢过该用户了，请勿重复操作").build());
        }
        return ResponseEntity.status(500).body(R.code(500).message("未知错误，请联系管理员或稍后再试").build());
    }

    @ApiOperation(value = "取消喜欢某人", notes = "会让该用户字段：likeNum（喜欢人数）-1")
    @DeleteMapping("/like/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity cancelLikeSomeone(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long uid) {
        UserReactService.STATUS status = userReactService.cancelLikeMe(authUser.getUid(), uid);
        switch (status) {
            case contradiction:
                return ResponseEntity.badRequest().body(R.code(4000).message("不可以对自己操作").build());
            case success:
                return ResponseEntity.ok().body(R.code(200).message("取消喜欢成功，该用户被喜欢次数 -1").build());
            case not_exist:
                return ResponseEntity.badRequest().body(R.code(4001).message("您还未喜欢过该用户了，请勿进行此操作").build());
        }
        return ResponseEntity.status(500).body(R.code(500).message("未知错误，请联系管理员或稍后再试").build());
    }
}
