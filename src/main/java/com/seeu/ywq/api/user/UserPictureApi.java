package com.seeu.ywq.api.user;

import com.seeu.ywq.user.dto.PhotoWallVO;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
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
