package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.user.dvo.TagVO;
import com.seeu.ywq.user.model.Tag;
import com.seeu.ywq.user.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 2:27 PM
 * Describe:
 */

@Api(tags = "用户-标签信息", description = "用户标签修改")
@RestController("adminUserTagApi")
@RequestMapping("/api/admin/v1/tag")
@PreAuthorize("hasRole('ADMIN')")
public class UserTagApi {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public List<Tag> listAll() {
        return tagService.findAllTags();
    }

    @ApiOperation("添加")
    @PostMapping
    public TagVO add(String tagName) {
        Tag tag = new Tag();
        tag.setCreateTime(new Date());
        tag.setDeleteFlag(Tag.DELETE_FLAG.show);
        tag.setTagName(tagName);
        return tagService.add(tag);
    }

    @ApiOperation("硬删除")
    @DeleteMapping("/{tagId}/hard")
    public ResponseEntity deleteHard(@PathVariable Long tagId) {
        tagService.delete(tagId);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }

    @ApiOperation("软删除")
    @DeleteMapping("/{tagId}")
    public ResponseEntity delete(@PathVariable Long tagId) {
        tagService.deleteFake(tagId);
        return ResponseEntity.ok(R.code(200).message("软删除成功"));
    }
}
