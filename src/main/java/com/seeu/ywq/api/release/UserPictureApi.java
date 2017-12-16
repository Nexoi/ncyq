package com.seeu.ywq.api.release;

import com.seeu.ywq.release.service.UserPictureService;

import javax.annotation.Resource;

//@Api(tags = "用户信息", description = "用户信息查看/修改")
//@RestController
//@RequestMapping("/api/v1/release")
public class UserPictureApi {

    @Resource
    private UserPictureService userPictureService;
//
//    @GetMapping("/{uid}/picture")
//    public ResponseEntity getAll(@PathVariable("uid") Long uid) {
//        Page page = userPictureService.findAllByUid(uid, 3l, new PageRequest(0, 10));
//        return ResponseEntity.ok(page);
//    }
}
