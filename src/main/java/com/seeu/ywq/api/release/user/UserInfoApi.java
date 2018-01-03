package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.user.dvo.PhotoWallVO;
import com.seeu.ywq.user.dvo.UserVO;
import com.seeu.ywq.user.dvo.UserWithNickName;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.user.service.UserPhotoWallService;
import com.seeu.ywq.userlogin.dvo.UserLoginVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.model.UserVIP;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.userlogin.service.UserVIPService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户基本信息的CRUD
 */
@Api(tags = "用户信息", description = "用户信息查看/修改")
@RestController("userInfoApi")
@RequestMapping("/api/v1/user")
public class UserInfoApi {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserPhotoWallService userPhotoWallService;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private UserVIPService userVIPService;

    /**
     * 查看用户所有信息【本人】
     *
     * @param authUser
     * @return
     */
    @ApiOperation(value = "查看用户所有信息【本人】", notes = "查看用户个人信息，用户需要处于已登陆状态。信息包含：基本信息（认证、技能）、相册，不包含动态")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "无用户信息")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMineAll(@ApiParam(hidden = true) @AuthenticationPrincipal UserLogin authUser) {
        Long uid = authUser.getUid();
        UserVO user = userInfoService.findOne(uid);
        if (user == null)
            return ResponseEntity.status(404).body(R.code(404).message("无此用户信息 [UID = " + uid + "]").build());
        // 其余信息
        List<PhotoWallVO> userAlbums = userPhotoWallService.findAllByUid(uid);
        UserLoginVO ul = userReactService.findOneWithSafety(uid);
        UserVIP vip = userVIPService.findOne(uid);
        if (vip == null) {
            vip = new UserVIP();
            vip.setTerminationDate(null);
            vip.setVipLevel(UserVIP.VIP.none);
        }
        Map map = new HashMap();
        map.put("info", user);
        map.put("basicInfo", ul);
        map.put("albums", userAlbums);
        map.put("vip", vip);
        return ResponseEntity.ok(map);
    }

    /**
     * 查看用户所有信息【非本人】
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "查看用户所有信息", notes = "查看该用户个人信息。信息包含：基本信息（认证、技能）、相册，不包含动态")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "无用户信息")
    })
    @GetMapping("/{uid}/all")
    public ResponseEntity getMineAll(@PathVariable("uid") Long uid) {
        UserVO user = userInfoService.findOne(uid);
        if (user == null)
            return ResponseEntity.status(404).body(R.code(404).message("无此用户信息 [UID = " + uid + "]").build());

        // 其余信息
        List<PhotoWallVO> userAlbums = userPhotoWallService.findAllByUid(uid);
        Map map = new HashMap();
        map.put("info", user);
        map.put("albums", userAlbums);
        return ResponseEntity.ok(map);
    }

    /**
     * 查看用户信息【本人】
     *
     * @param authUser
     * @return
     */
    @ApiOperation(value = "查看用户信息【本人】", notes = "查看用户个人信息，用户需要处于已登陆状态")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "无用户信息")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMine(@ApiParam(hidden = true) @AuthenticationPrincipal UserLogin authUser) {
        UserVO user = userInfoService.findOne(authUser.getUid());
        return user == null ? ResponseEntity.status(404).body(R.code(404).message("无此用户信息 [UID = " + authUser.getUid() + "]").build()) : ResponseEntity.ok(user);
    }

    /**
     * 查看用户信息【非本人】
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "查看用户信息", notes = "查看该用户个人信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "无此用户信息")
    })
    @GetMapping("/{uid}")
    public ResponseEntity get(@PathVariable("uid") Long uid) {
        UserVO user = userInfoService.findOne(uid);
        return user == null ? ResponseEntity.status(404).body(R.code(404).message("无此用户信息 [UID = " + uid + "]").build()) : ResponseEntity.ok(user);
    }

    /**
     * 存储/更新用户信息【本人】
     *
     * @param authUser
     * @param user
     * @return
     */
    @ApiOperation(value = "存储/更新用户信息【本人】", notes = "存储/更新用户个人信息，用户需要处于已登陆状态")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity saveOrUpdate(@ApiParam(hidden = true) @AuthenticationPrincipal UserLogin authUser,
                                       User user,
                                       @RequestParam(required = false) String nickname) {
        User sourceUser = userInfoService.findOneInfo(authUser.getUid());
        user.setPhone(null); // 电话号码不可修改
        user.setFansNum(null);
        user.setFollowNum(null);
        user.setPublishNum(null);
        user.setTags(null);
//        user.setSkills(null);
        user.setUid(authUser.getUid());
        BeanUtils.copyProperties(user, sourceUser);
        User savedUser = userInfoService.saveInfo(sourceUser);
        // 昵称修改
        if (nickname != null) {
            UserLogin ul = userReactService.saveNickName(authUser.getUid(), nickname);
            if (ul != null) {
                UserWithNickName userWithNickName = new UserWithNickName();
                BeanUtils.copyProperties(savedUser, userWithNickName);
                userWithNickName.setNickname(ul.getNickname());
                return ResponseEntity.ok(userWithNickName);
            }
        }
        return ResponseEntity.ok(savedUser);
    }

    @ApiOperation(value = "上传头像（更新）【本人】", notes = "存储/更新用户头像信息，用户需要处于已登陆状态")
    @PostMapping("/head-icon")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity uploadHeadIcon(@AuthenticationPrincipal UserLogin authUser, MultipartFile image) {
        String url = userInfoService.updateHeadIcon(authUser.getUid(), image);
        return (url == null)
                ? ResponseEntity.badRequest().body(R.code(400).message("头像更新失败").build())
                : ResponseEntity.ok(R.code(200).message("头像更新成功！").build());
    }

    @ApiOperation(value = "设定性别【本人】", notes = "每个用户只能设定一次性别，不可修改，用户需要处于已登陆状态")
    @PostMapping("/gender")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity setGender(@AuthenticationPrincipal UserLogin authUser, UserLogin.GENDER gender) {
        UserInfoService.STATUS status = userInfoService.setGender(authUser.getUid(), gender);
        switch (status) {
            case success:
                return ResponseEntity.ok(R.code(200).message("设置成功！").build());
            case has_set:
                return ResponseEntity.badRequest().body(R.code(4000).message("性别设定之后不可修改").build());
            case failure:
            default:
                return ResponseEntity.badRequest().body(R.code(4001).message("设定失败，请稍后再试").build());
        }
    }

}
