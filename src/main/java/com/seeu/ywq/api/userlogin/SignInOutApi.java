package com.seeu.ywq.api.userlogin;

import com.seeu.core.R;
import io.swagger.annotations.Api;
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
@Api(tags = "登录", description = "登录/注销")
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
    public ResponseEntity signOut() {
        return ResponseEntity.ok(R.code(200).message("注销成功").build());
    }


    @GetMapping("/signin-success")
    @ApiResponse(code = 200, message = "登陆成功")
    public ResponseEntity signInSuccess() {
        return ResponseEntity.ok().body(R.code(200).message("登陆成功").build());
    }

    @GetMapping("/signin-failure")
    @ApiResponse(code = 400, message = "登陆失败，账号/密码错误")
    public ResponseEntity signInFailure() {
        return ResponseEntity.badRequest().body(R.code(400).message("登陆失败，账号/密码错误").build());
    }

    @GetMapping("/signout-success")
    @ApiResponse(code = 200, message = "注销成功")
    public ResponseEntity signOutSuccess() {
        return ResponseEntity.ok(R.code(200).message("注销成功").build());
    }
}
