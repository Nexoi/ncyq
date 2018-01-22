package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.user.service.UserPhotoWallService;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.service.UserVIPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 10:13 AM
 * Describe:
 */
@Api(tags = "用户信息", description = "用户基本信息／标签／认证（粉丝／喜欢的具体信息不展示）")
@RestController("adminUserInfoApi")
@RequestMapping("/api/admin/v1/user")
public class UserInfoApi {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserPhotoWallService userPhotoWallService;
    @Autowired
    private UserVIPService userVIPService;


    @ApiOperation("查詢用戶信息（具備搜索功能，默認按粉絲數排序）")
    @GetMapping("/info/list")
    public ResponseEntity search(@RequestParam(required = false) String word,
                                 @RequestParam(required = false) USER search,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 @RequestParam(required = false) String orderBy,
                                 @RequestParam(required = false) Sort.Direction direction) {
        if (orderBy == null) orderBy = "fansNum";
        if (direction == null) direction = Sort.Direction.DESC;
        PageRequest request = new PageRequest(page, size, new Sort(direction, orderBy));
        if (null == search || null == word) {
            return ResponseEntity.ok(userInfoService.findAll(request));
        } else {
            return ResponseEntity.ok(userInfoService.searchAll(search, word, request));
        }
    }

    @ApiOperation("查詢某一個用戶的數據，包含用戶VIP、標簽、認證信息")
    @GetMapping("/info/{uid}")
    public ResponseEntity get(@PathVariable Long uid) {
        UserVO user = userInfoService.findOne(uid);
        if (user == null)
            return ResponseEntity.status(404).body(R.code(404).message("無此用戶"));
        UserVIP vip = userVIPService.findOne(uid);
        if (vip == null) {
            vip = new UserVIP();
            vip.setVipLevel(UserVIP.VIP.none);
            vip.setUpdateTime(new Date());
            vip.setUid(uid);
            vip.setTerminationDate(null);
        }
        Map map = new HashMap();
        map.put("info", user);
        map.put("vip", vip);
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "激活／删除用户 VIP 信息")
    @PutMapping("/vip/{uid}")
    public ResponseEntity updateVIP(@PathVariable Long uid,
                                    @RequestParam(required = false) Long day,
                                    @RequestParam(required = true) UserVIP.VIP vip) {
        if (vip == UserVIP.VIP.vip && day == null)
            return ResponseEntity.status(400).body(R.code(4000).message("天数不能为空"));
        if (vip == UserVIP.VIP.vip) {
            userVIPService.active(uid, day);
            return ResponseEntity.ok(R.code(200).message("更新 VIP 成功！"));
        } else {
            try {
                userVIPService.disable(uid);
                return ResponseEntity.ok(R.code(200).message("删除用户 VIP 信息成功！"));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(404).body(R.code(404).message("用户没有 VIP 信息"));
            }
        }
    }

    @ApiOperation(value = "查询某个用户的照片墙")
    @GetMapping("/photo/{uid}")
    public ResponseEntity photos(@PathVariable Long uid) {
        return ResponseEntity.ok(userPhotoWallService.findAllByUid(uid));
    }

    @ApiOperation(value = "删除某个用户的照片墙上的照片")
    @GetMapping("/photo/{uid}/{id}")
    public ResponseEntity deletePhotos(@PathVariable Long uid, @PathVariable Long id) {
        userPhotoWallService.delete(uid, id);
        return ResponseEntity.ok(R.code(200).message("删除成功！"));
    }
}
