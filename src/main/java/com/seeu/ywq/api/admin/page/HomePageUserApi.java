package com.seeu.ywq.api.admin.page;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page_home.model.HomeUser;
import com.seeu.ywq.page_home.service.HomeUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 5:44 PM
 * Describe:
 */
@Api(tags = "首页人物配置", description = "首页")
@RestController("adminHomePageUsersApi")
@RequestMapping("/api/admin/v1/app/home")
//@PreAuthorize("hasRole('ADMIN')")
public class HomePageUserApi {

    @Autowired
    private HomeUserService homeUserService;

    @GetMapping("/list")
    public ResponseEntity list(@RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(homeUserService.findAll(new PageRequest(page, size)));
    }


    @GetMapping("/{uid}")
    public ResponseEntity get(@PathVariable Long uid) {
        try {
            return ResponseEntity.ok(homeUserService.findOne(uid));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该配置"));
        }
    }


    @PostMapping
    public ResponseEntity add(Long uid,
                              HomeUser.LABEL label,
                              String coverImageUrl,
                              @RequestParam(required = false) String videoUrl) {
        try {
            return ResponseEntity.ok(homeUserService.save(uid, label, coverImageUrl, videoUrl));
        } catch (ActionParameterException e) {
            return ResponseEntity.badRequest().body(R.code(4000).message("参数错误"));
        } catch (ResourceAlreadyExistedException e) {
            return ResponseEntity.badRequest().body(R.code(4001).message("该资源已存在，请勿重复设定"));
        }
    }

    @PutMapping("/{uid}")
    public ResponseEntity update(@PathVariable Long uid,
                                 HomeUser.LABEL label,
                                 String coverImageUrl,
                                 @RequestParam(required = false) String videoUrl) {
        try {
            return ResponseEntity.ok(homeUserService.update(uid, label, coverImageUrl, videoUrl));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该配置"));
        }
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity delete(@PathVariable Long uid) {
        try {
            homeUserService.delete(uid);
            return ResponseEntity.ok(R.code(200).message("删除成功"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该配置"));
        }
    }

}
