package com.seeu.ywq.api.user;

import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 该 api 登陆/注销功能已由 spring security 自动实现
 */
@RestController
public class SignInOutApi {

    @GetMapping("/signin-success")
    @ApiResponse(code = 200, message = "sign in success")
    public ResponseEntity signInSuccess() {
        return ResponseEntity.ok().body("sign in success");
    }

    @GetMapping("/signin-failure")
    @ApiResponse(code = 400, message = "sign in failure")
    public ResponseEntity signInFailure() {
        return ResponseEntity.badRequest().body("sign in failure, please try again.");
    }

    @GetMapping("/signout-success")
    @ApiResponse(code = 200, message = "sign out success")
    public ResponseEntity signOutSuccess() {
        return ResponseEntity.ok("sign out success");
    }
}
