package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.IdentificationApplyRepeatException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dto.IdentificationApplyDTO;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.service.IdentificationService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "用户个人认证", description = "个人认证列表查询，个人认证信息查询，申请")
@RestController
@RequestMapping("/api/v1/identification")
public class UserIdentificationApi {

    @Autowired
    private IdentificationService identificationService;

    @ApiOperation(value = "查看所有认证信息")
    @GetMapping("/list")
    public ResponseEntity listAll() {
        List list = identificationService.findAll();
        return list == null || list.size() == 0 ? ResponseEntity.status(404).body(R.code(404).message("无认证列表可用").build()) : ResponseEntity.ok(list);
    }

    @ApiOperation("查看某用户的认证信息（仅能看到已经审核通过的）")
    @GetMapping("/{uid}")
    public ResponseEntity listOnes(@PathVariable Long uid) {
        List list = identificationService.findAllAccessByUid(uid);
        return ResponseEntity.ok(list);
    }

    @ApiOperation("查看自己的认证列表信息")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listMine(@AuthenticationPrincipal UserLogin authUser) {
        List list = identificationService.findAllByUid(authUser.getUid());
        return ResponseEntity.ok(list);
    }

    @ApiOperation("查看自己的认证列表信息【附上所有认证列表信息】")
    @GetMapping("/with-identificationList")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listMineWithIdentificationList(@AuthenticationPrincipal UserLogin authUser) {
        List list = identificationService.findAllWithFullIdentificationInfoByUid(authUser.getUid());
        return ResponseEntity.ok(list);
    }

    @ApiOperation("认证信息上传")
    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity apply(@AuthenticationPrincipal UserLogin authUser,
                                @Validated IdentificationApply identificationApply,
                                @ApiParam(hidden = true)
                                @RequestParam(required = false) MultipartFile transferVoucherImage, // 转账图片
                                MultipartFile frontImage,
                                MultipartFile backImage) {
        if (frontImage == null || backImage == null)
            return ResponseEntity.badRequest().body(R.code(4000).message("身份证图片不能为空").build());
//        if (transferVoucherImage == null)
//            return ResponseEntity.badRequest().body(R.code(4001).message("转账图片不能为空").build());
        if (identificationApply.getIdentificationId() == null)
            return ResponseEntity.badRequest().body(R.code(4002).message("认证 ID 不能为空").build());
        identificationApply.setUid(authUser.getUid()); // set to myself
        try {
            IdentificationApply apply = identificationService.apply(identificationApply.getIdentificationId(), authUser.getUid(), identificationApply, frontImage, backImage, transferVoucherImage);
            return apply == null ? ResponseEntity.badRequest().body(R.code(4003).message("申请数据加载失败，请重新上传数据").build()) : ResponseEntity.ok(apply);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(R.code(500).message("服务器内部异常，请联系管理员").build());
        } catch (IdentificationApplyRepeatException e) {
            return ResponseEntity.badRequest().body(R.code(4004).message("认证信息已提交，不能重复申请认证").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(4005).message("无此认证信息，请确认认证 ID 是否正确").build());
        }
    }

    @ApiOperation("取消认证信息")
    @DeleteMapping("/apply/{identificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity cancelApply(@AuthenticationPrincipal UserLogin authUser,
                                      @PathVariable Long identificationId) {
        try {
            identificationService.deleteApply(authUser.getUid(), identificationId);
            return ResponseEntity.ok(R.code(200).message("认证取消成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("您未申请过该认证").build());
        }
    }

    @ApiOperation(value = "认证信息下载", notes = "以最新上传的一次为主")
    @GetMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyApplyInfo(@AuthenticationPrincipal UserLogin authUser) {
        IdentificationApply apply = identificationService.findMyRecentInfo(authUser.getUid());
        return apply == null ? ResponseEntity.status(404).body(R.code(404).message("您还未上传认证信息，请上传后重试").build()) : ResponseEntity.ok(apply);
    }
}
