package com.seeu.ywq.api.release.message;

import com.seeu.core.R;
import com.seeu.ywq.message.model.PersonalMessage;
import com.seeu.ywq.message.model.SysMessage;
import com.seeu.ywq.message.service.PersonalMessageService;
import com.seeu.ywq.message.service.SysMessageService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.utils.DateFormatterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 5:14 PM
 * Describe:
 */

@Api(tags = "APP消息推送列表", description = "个人消息／系统消息")
@RestController
@RequestMapping("/api/v1/message")
public class MessageApi {

    @Autowired
    private DateFormatterService dateFormatterService;
    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private PersonalMessageService personalMessageService;

    @ApiOperation(value = "获取系统通知【按时间】", notes = "date format: yyyy-MM-dd HH:mm:ss")
    @GetMapping("/system/by-date")
    public ResponseEntity listSys(@RequestParam(required = true) String date) {
        try {
            Date date1 = dateFormatterService.getyyyyMMddHHmmss().parse(date);
            return ResponseEntity.ok(sysMessageService.findByDate(date1));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("时间参数解析错误"));
        }
    }

    @ApiOperation(value = "获取系统通知【分页】")
    @GetMapping("/system/list")
    public Page<SysMessage> listAllSys(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return sysMessageService.findAll(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime")));
    }

    @ApiOperation(value = "获取个人通知【按时间】", notes = "date format: yyyy-MM-dd HH:mm:ss")
    @GetMapping("/personal/by-date")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listPersonal(@AuthenticationPrincipal UserLogin authUser,
                                       @RequestParam PersonalMessage.TYPE[] types,
                                       @RequestParam(required = true) String date) {
        try {
            Date date1 = dateFormatterService.getyyyyMMddHHmmss().parse(date);
            return ResponseEntity.ok(personalMessageService.findAll(authUser.getUid(), Arrays.asList(types), date1));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("时间参数解析错误"));
        }
    }

//
//    @ApiOperation(value = "获取个人通知【分页/按类别】")
//    @GetMapping("/personal/list")
//    @PreAuthorize("hasRole('USER')")
//    public List<PersonalMessage> listAllPersonalByType(@AuthenticationPrincipal UserLogin authUser,
//                                                       @RequestParam PersonalMessage.TYPE[] types,
//                                                       @RequestParam(defaultValue = "0") Integer page,
//                                                       @RequestParam(defaultValue = "10") Integer size) {
//        return personalMessageService.findAll(authUser.getUid(), Arrays.asList(types), new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime")));
//    }

    @ApiOperation(value = "获取个人通知【分页】")
    @GetMapping("/personal/list")
    @PreAuthorize("hasRole('USER')")
    public Page<PersonalMessage> listAllPersonal(@AuthenticationPrincipal UserLogin authUser,
                                                 @RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return personalMessageService.findAll(authUser.getUid(), new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime")));
    }

    @ApiOperation("获取未读消息条数")
    @GetMapping("/counts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listAllCounts(@AuthenticationPrincipal UserLogin authUser,
                                        @RequestParam(required = true) String date) {

        Date date1 = null;
        try {
            date1 = dateFormatterService.getyyyyMMddHHmmss().parse(date);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("时间格式错误"));
        }
        return ResponseEntity.ok(personalMessageService.countAll(authUser.getUid(), date1));
    }
}
