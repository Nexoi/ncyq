package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.admin.service.BindUserService;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.userlogin.exception.NickNameSetException;
import com.seeu.ywq.userlogin.exception.PasswordSetException;
import com.seeu.ywq.userlogin.exception.PhoneNumberHasUsedException;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 10:13 AM
 * Describe:
 */
@Api(tags = "用户-基础信息", description = "用户登录信息")
@RestController("adminUserLoginApi")
@RequestMapping("/api/admin/v1/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserLoginApi {
    @Autowired
    private UserReactService userReactService;


    @ApiOperation("查詢用戶信息（按照 uid 可搜索）")
    @GetMapping("/basic/list")
    public ResponseEntity search(@RequestParam(required = false) String word,
                                 @RequestParam(required = false) USERLogin search,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 @RequestParam(required = false) String orderBy,
                                 @RequestParam(required = false) Sort.Direction direction) {
        if (orderBy == null) orderBy = "uid";
        if (direction == null) direction = Sort.Direction.DESC;
        PageRequest request = new PageRequest(page, size, new Sort(direction, orderBy));
        if (null == search || null == word) {
            return ResponseEntity.ok(userReactService.findAll(request));
        } else {
            try {
                return ResponseEntity.ok(userReactService.searchAll(search, word, request));
            } catch (ActionParameterException e) {
                return ResponseEntity.status(400).body(R.code(400).message("搜索 uid 或 phone 时必须传入数字"));
            }
        }
    }

    @ApiOperation("查詢某一個用戶的數據，包含账号（手机号）密码等信息")
    @GetMapping("/basic/{uid}")
    public ResponseEntity get(@PathVariable Long uid) {
        UserLogin user = userReactService.findOne(uid);
        if (user == null)
            return ResponseEntity.status(404).body(R.code(404).message("無此用戶"));
        return ResponseEntity.ok(user);
    }


    @Autowired
    private BindUserService bindUserService;
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "添加用户", notes = "初始化权限为基本用户")
    @PostMapping
    public ResponseEntity add(@RequestParam(required = true) String phone,
                              @RequestParam(required = true) String nickname,
                              @RequestParam(required = true) String password,
                              @RequestParam(required = false) UserLogin.GENDER gender,
                              @RequestParam(required = false) String headIconUrl,
                              User user,
                              @AuthenticationPrincipal UserLogin authUser) {
        try {
            UserLogin ul = userReactService.add(phone, nickname, password, gender, headIconUrl, user);
            bindUserService.bind(authUser.getUid(), ul.getUid());
            return ResponseEntity.ok(ul);
        } catch (NickNameSetException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("用户名不可为空"));
        } catch (PhoneNumberHasUsedException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("手机号码不可为空"));
        } catch (PasswordSetException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("密码需大于 6 位"));
        }
    }

    @ApiOperation(value = "修改用户信息")
    @PutMapping("/{uid}")
    public ResponseEntity updateUser(@PathVariable Long uid,
                                     @RequestParam(required = false) String phone,
                                     @RequestParam(required = false) String nickname,
                                     @RequestParam(required = false) UserLogin.GENDER gender,
                                     @RequestParam(required = false) String headIconUrl,
                                     User user,
                                     @AuthenticationPrincipal UserLogin authUser) {
        UserLogin ul = userReactService.findOne(uid);
        if (ul == null)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该用户"));
        if (!bindUserService.canOperateUser(authUser.getUid(), uid))
            return ResponseEntity.badRequest().body(R.code(4001).message("无权操作该用户数据"));
        if (headIconUrl != null) ul.setHeadIconUrl(headIconUrl);
        if (gender != null) ul.setGender(gender);
        if (nickname != null) ul.setNickname(nickname);
        if (phone != null) ul.setPhone(phone);
        userReactService.save(ul);
        user.setUid(uid);
        userInfoService.saveInfo(user);
        return ResponseEntity.ok(R.code(200).message("更新成功"));
    }

}
