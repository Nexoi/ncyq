package com.seeu.ywq.api.admin.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.model.Identification;
import com.seeu.ywq.user.model.IdentificationApply;
import com.seeu.ywq.user.service.IdentificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 2:27 PM
 * Describe:
 */

@Api(tags = "用户-认证信息", description = "用户认证／申请")
@RestController("adminUserIdentificationApi")
@RequestMapping("/api/admin/v1/identification")
public class UserIdentificationApi {

    @Autowired
    private IdentificationService identificationService;

    @GetMapping("/list")
    public List<Identification> listAll() {
        return identificationService.findAll();
    }

    @ApiOperation("添加")
    @PostMapping
    public Identification add(String identificationName, String iconUrl, String iconActiveUrl) {
        Identification id = new Identification();
        id.setIconActiveUrl(iconActiveUrl);
        id.setIconUrl(iconUrl);
        id.setIdentificationName(identificationName);
        return identificationService.save(id);
    }


    @ApiOperation("修改（若不存在则执行添加）")
    @PutMapping("/{identificationId}")
    public Identification update(@PathVariable Long identificationId,
                                 String identificationName, String iconUrl, String iconActiveUrl) {
        Identification id = new Identification();
        id.setId(identificationId);
        id.setIconActiveUrl(iconActiveUrl);
        id.setIconUrl(iconUrl);
        id.setIdentificationName(identificationName);
        return identificationService.save(id);
    }

    @ApiOperation("删除")
    @DeleteMapping("/{identificationId}")
    public ResponseEntity deleteHard(@PathVariable Long identificationId) {
        identificationService.delete(identificationId);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }

    // apply list //


    @ApiOperation("列出所有")
    @GetMapping("/apply/list")
    public Page<IdentificationApply> listAllApply(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return identificationService.findAllApply(new PageRequest(page, size));
    }

    @ApiOperation("按状态列出")
    @GetMapping("/apply/list/by-status")
    public Page<IdentificationApply> listAllApplyByStatuses(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size,
                                                            @RequestParam IdentificationApply.STATUS[] statuses) {
        return identificationService.findAllApply(Arrays.asList(statuses), new PageRequest(page, size));
    }

    @ApiOperation("按用户列出")
    @GetMapping("/apply/list/{uid}")
    public Page<IdentificationApply> listAllApplyByUid(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size,
                                                       @PathVariable Long uid) {
        return identificationService.findAllApply(uid, new PageRequest(page, size));
    }

    @ApiOperation("申请通过")
    @DeleteMapping("/apply/pass")
    public ResponseEntity pass(@PathVariable Long uid, Long identificationId) {
        try {
            identificationService.pass(uid, identificationId);
            return ResponseEntity.ok(R.code(200).message("通过！"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("该用户未提交申请！"));
        }
    }

    @ApiOperation("否定申请")
    @DeleteMapping("/apply/fail")
    public ResponseEntity fail(@PathVariable Long uid, Long identificationId) {
        try {
            identificationService.fail(uid, identificationId);
            return ResponseEntity.ok(R.code(200).message("拒绝申请成功！"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("该用户未提交申请！"));
        }
    }
}
