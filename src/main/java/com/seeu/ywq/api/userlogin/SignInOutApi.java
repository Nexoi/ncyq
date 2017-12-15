package com.seeu.ywq.api.userlogin;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 该 api 登陆/注销功能已由 spring security 自动实现
 */
@RestController
public class SignInOutApi {

    @ApiResponse(code = 200, message = "登陆成功")
    @ApiOperation(value = "登录账号", notes = "登陆成功只会返回 200 状态码，token 信息会自动写入 cookie，客户端需要支持 cookie；如需要退出账号，请使用 /api/v1/signout 清除 cookie 信息")
    @PostMapping("/api/v1/signin")
    public void signIn(@RequestParam String phone,
                       @RequestParam String password) {
        return;
    }

    @ApiResponse(code = 200, message = "注销成功")
    @ApiOperation(value = "注销用户", notes = "退出登陆时调用此接口，可以清除服务器缓存信息，用户需再次登录获取新的 token 才能继续访问")
    @PostMapping("/api/v1/signout")
    public void signOut() {
        return;
    }


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
