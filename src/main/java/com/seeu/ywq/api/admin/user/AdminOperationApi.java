package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.admin.service.BindUserService;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.userlogin.exception.NickNameSetException;
import com.seeu.ywq.userlogin.exception.PasswordSetException;
import com.seeu.ywq.userlogin.exception.PhoneNumberHasUsedException;
import com.seeu.ywq.userlogin.model.UserAuthRole;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserAuthRoleRepository;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 08/02/2018
 * Time: 12:10 AM
 * Describe:
 */

@Api(tags = "管理员操作接口", description = "管理员操作")
@RestController("adminOperateApi")
@RequestMapping("/api/admin/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOperationApi {

    @Autowired
    private UserReactService userReactService;
    @Autowired
    private BindUserService bindUserService;
    @Autowired
    private UserAuthRoleRepository userAuthRoleRepository;

    @ApiOperation("新建管理员")
    @PostMapping
    public ResponseEntity add(@RequestParam(required = true) String phone,
                              @RequestParam(required = true) String nickname,
                              @RequestParam(required = true) String password,
                              @RequestParam(required = false) UserLogin.GENDER gender,
                              @RequestParam(required = false) String headIconUrl,
                              User user) {
        try {
            UserLogin ul = userReactService.add(phone, nickname, password, gender, headIconUrl, user);
            // 增加权限
            List<UserAuthRole> roles = new ArrayList<>();
            roles.add(userAuthRoleRepository.findByName("ROLE_USER"));
            roles.add(userAuthRoleRepository.findByName("ROLE_ADMIN"));
            ul.setRoles(roles);
            ul = userReactService.save(ul);
            return ResponseEntity.ok(ul);
        } catch (NickNameSetException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("用户名不可为空"));
        } catch (PhoneNumberHasUsedException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("手机号码不可为空"));
        } catch (PasswordSetException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("密码需大于 6 位"));
        }
    }

    @ApiOperation("查看自己可以管理的用户（绑定用户）")
    @GetMapping("/list-users")
    public Page<UserLogin> list(@AuthenticationPrincipal UserLogin authUser,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        List<Long> uids = bindUserService.findAll(authUser.getUid());
        return userReactService.findAllByUids(uids, new PageRequest(page, size));
    }
}
