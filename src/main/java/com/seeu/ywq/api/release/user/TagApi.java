package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.user.dvo.TagVO;
import com.seeu.ywq.user.service.TagService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 只是提供给用户使用，不需要提供管理员 API
 * <p>
 * 标签信息只能查看
 */
@Api(tags = "用户标签", description = "查看标签、添加自己的关注标签、设定自己的标签等")
@RestController
@RequestMapping("/api/v1/tag")
public class TagApi {
    @Autowired
    TagService tagService;

    @ApiOperation(value = "按分页查询", notes = "默认第 0 页，每页 10 条")
    @GetMapping("/withpage")
    public ResponseEntity listPage(@RequestParam(defaultValue = "0", required = false) Integer page,
                                   @RequestParam(defaultValue = "10", required = false) Integer size) {
        return ResponseEntity.ok(tagService.findAll(new PageRequest(page, size)));
    }

    @ApiOperation(value = "查看所有标签", notes = "会列出系统管理员设定的所有标签")
    @GetMapping("/all")
    public ResponseEntity list() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @ApiOperation(value = "按 ID 查询标签", notes = "查看该 ID 的标签信息")
    @ApiResponse(code = 404, message = "找不到该标签")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        TagVO tag = tagService.findOne(id);
        return tag == null ? ResponseEntity.status(404).body(R.code(404).message("找不到该标签").build()) : ResponseEntity.ok(tag);
    }

    @ApiOperation(value = "查看自己添加的标签", notes = "查看自己添加的标签信息，性别必须为：女。")
    @GetMapping("/set")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMine(@AuthenticationPrincipal UserLogin authUser) {
        if (authUser.getGender() == UserLogin.GENDER.male)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        List list = tagService.findAllMine(authUser.getUid());
        return list == null || list.size() == 0 ? ResponseEntity.status(404).body(R.code(404).message("您还未添加任何标签").build()) : ResponseEntity.ok(list);
    }

    @ApiOperation(value = "查看自己关注的标签", notes = "查看自己关注的所有标签信息，性别必须为：男。")
    @GetMapping("/follow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getFocus(@AuthenticationPrincipal UserLogin authUser) {
        if (authUser.getGender() == UserLogin.GENDER.female)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        List list = tagService.findAllFocus(authUser.getUid());
        return list == null || list.size() == 0 ? ResponseEntity.status(404).body(R.code(404).message("您还未关注任何标签").build()) : ResponseEntity.ok(list);
    }

    @ApiOperation(value = "添加标签", notes = "为自己添加标签信息，性别必须为：女。若上传标签重复，依然会返回“添加成功”信息")
    @ApiResponse(code = 400, message = "数据错误")
    @PostMapping("/set")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity addMyTag(@AuthenticationPrincipal UserLogin authUser,
                                   @ApiParam(value = "需要添加的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                   @RequestParam Long[] ids) {
        // 判断性别
        if (authUser.getGender() == UserLogin.GENDER.male)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(4002).message("添加的 id 数组不能为空").build());
        // 添加
        TagService.STATUS status = tagService.addMine(authUser.getUid(), ids);
        switch (status) {
            case success:
                return ResponseEntity.ok(R.code(200).message("添加成功").build());
            case hasAdded:
                return ResponseEntity.badRequest().body(R.code(4003).message("有已经添加过的标签，请勿重复添加").build());
            case failure:
            default:
                return ResponseEntity.badRequest().body(R.code(4004).message("添加失败，请稍后再试").build());
        }
    }

    @ApiOperation(value = "添加标签【重置】", notes = "为自己添加标签信息，性别必须为：女。")
    @ApiResponse(code = 400, message = "数据错误")
    @PostMapping("/set-withclean")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity resetMyTag(@AuthenticationPrincipal UserLogin authUser,
                                   @ApiParam(value = "需要添加的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                   @RequestParam Long[] ids) {
        // 判断性别
        if (authUser.getGender() == UserLogin.GENDER.male)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(4002).message("添加的 id 数组不能为空").build());
        // 添加
        TagService.STATUS status = tagService.resetMine(authUser.getUid(), ids);
        switch (status) {
            case success:
                return ResponseEntity.ok(R.code(200).message("添加成功").build());
            case hasAdded:
                return ResponseEntity.badRequest().body(R.code(4003).message("有已经添加过的标签，请勿重复添加").build());
            case failure:
            default:
                return ResponseEntity.badRequest().body(R.code(4004).message("添加失败，请稍后再试").build());
        }
    }

    @ApiOperation(value = "关注标签", notes = "为自己添加关注的标签信息，性别必须为：男。若上传标签重复，依然会返回“关注成功”信息")
    @ApiResponse(code = 400, message = "数据错误")
    @PostMapping("/follow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity addFocusTag(@AuthenticationPrincipal UserLogin authUser,
                                      @ApiParam(value = "需要关注的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                      @RequestParam Long[] ids) {
        // 判断性别
        if (authUser.getGender() == UserLogin.GENDER.female)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(4002).message("添加的 id 数组不能为空").build());
        // 添加
        TagService.STATUS status = tagService.addFocus(authUser.getUid(), ids);
        switch (status) {
            case success:
                return ResponseEntity.ok(R.code(200).message("关注成功").build());
            case hasAdded:
                return ResponseEntity.badRequest().body(R.code(4003).message("有已经关注过的标签，请勿重复关注").build());
            case failure:
            case no_such_tag:
                return ResponseEntity.badRequest().body(R.code(4005).message("无此标签可以关注").build());
            default:
                return ResponseEntity.badRequest().body(R.code(4004).message("关注失败，请稍后再试").build());
        }
    }
    @ApiOperation(value = "关注标签【重置】", notes = "为自己添加关注的标签信息，性别必须为：男。")
    @ApiResponse(code = 400, message = "数据错误")
    @PostMapping("/follow-withclean")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity resetFocusTag(@AuthenticationPrincipal UserLogin authUser,
                                      @ApiParam(value = "需要关注的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                      @RequestParam Long[] ids) {
        // 判断性别
        if (authUser.getGender() == UserLogin.GENDER.female)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(4002).message("添加的 id 数组不能为空").build());
        // 添加
        TagService.STATUS status = tagService.resetFocus(authUser.getUid(), ids);
        switch (status) {
            case success:
                return ResponseEntity.ok(R.code(200).message("关注成功").build());
            case hasAdded:
                return ResponseEntity.badRequest().body(R.code(4003).message("有已经关注过的标签，请勿重复关注").build());
            case failure:
            case no_such_tag:
                return ResponseEntity.badRequest().body(R.code(4005).message("无此标签可以关注").build());
            default:
                return ResponseEntity.badRequest().body(R.code(4004).message("关注失败，请稍后再试").build());
        }
    }
    @ApiOperation(value = "删除添加的标签", notes = "删除自己添加的标签信息，性别必须为：女。")
    @ApiResponse(code = 400, message = "数据错误")
    @DeleteMapping("/set")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteMine(@AuthenticationPrincipal UserLogin authUser,
                                     @ApiParam(value = "需要删除的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                     @RequestParam Long[] ids) {
        if (authUser.getGender() == UserLogin.GENDER.male)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(400).message("参数 ids 必须为数组，用逗号隔开每一个值").build());
        List list = tagService.deleteMine(authUser.getUid(), ids);
        return list == null || list.size() == 0 ? ResponseEntity.ok().body(R.code(200).message("标签删除成功，您现在未添加任何标签").build()) : ResponseEntity.ok(R.code(200).message("标签删除成功").build());
    }

    @ApiOperation(value = "取消关注的标签", notes = "取消自己关注的标签信息，性别必须为：男。")
    @ApiResponse(code = 400, message = "数据错误")
    @DeleteMapping("/follow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteFocus(@AuthenticationPrincipal UserLogin authUser,
                                      @ApiParam(value = "需要删除的标签 id 信息，数组，用逗号隔开，如：2,5,3,17,6")
                                      @RequestParam Long[] ids) {
        if (authUser.getGender() == UserLogin.GENDER.female)
            return ResponseEntity.badRequest().body(R.code(4001).message("该性别不可进行该操作").build());
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(400).message("参数 ids 必须为数组，用逗号隔开每一个值").build());
        List list = tagService.deleteFocus(authUser.getUid(), ids);
        return list == null || list.size() == 0 ? ResponseEntity.ok().body(R.code(200).message("标签取消关注成功，您现在未关注任何标签").build()) : ResponseEntity.ok(R.code(200).message("标签取消关注成功").build());
    }
}
