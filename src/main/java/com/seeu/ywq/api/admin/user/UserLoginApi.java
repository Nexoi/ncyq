package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 10:13 AM
 * Describe:
 */
@Api(tags = "用户基础信息", description = "用户登录信息")
@RestController("adminUserLoginApi")
@RequestMapping("/api/admin/v1/user")
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

}
