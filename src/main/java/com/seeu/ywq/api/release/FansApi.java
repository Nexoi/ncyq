package com.seeu.ywq.api.release;

import com.seeu.core.R;
import com.seeu.ywq.release.service.FansService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FansApi {

    @Autowired
    private FansService fansService;

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

    @PostMapping("/follow/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity fansSomeOne(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long uid) {
        FansService.STATUS status = fansService.followSomeone(authUser.getUid(), uid);
        switch (status) {
            case success:
                return ResponseEntity.ok().body(R.code(200).message("关注成功！").build());
            case have_followed:
                return ResponseEntity.badRequest().body(R.code(400).message("您已经关注过该用户，无需重复关注").build());
            case not_such_person:
                return ResponseEntity.status(404).body(R.code(404).message("无此用户").build());
            case failure:
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }
}
