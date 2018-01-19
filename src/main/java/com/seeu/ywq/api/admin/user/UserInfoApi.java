package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.service.UserVIPService;
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

@RestController("adminUserApi")
@RequestMapping("/api/admin/v1/user")
public class UserInfoApi {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private UserVIPService userVIPService;


    @ApiOperation("查詢用戶信息（具備搜索功能，默認按粉絲數排序）")
    @GetMapping("/info/list")
    public ResponseEntity search(@RequestParam String word,
                                 @RequestParam USER search,
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

}
