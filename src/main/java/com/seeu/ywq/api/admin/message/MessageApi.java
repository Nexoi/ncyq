package com.seeu.ywq.api.admin.message;

import com.seeu.core.R;
import com.seeu.ywq.message.model.SysMessage;
import com.seeu.ywq.message.service.SysMessageService;
import com.seeu.ywq.utils.DateFormatterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 5:14 PM
 * Describe:
 */

@Api(tags = "消息推送列表", description = "个人消息／系统消息")
@RestController("adminMessageApi")
@RequestMapping("/api/admin/v1/message")
@PreAuthorize("hasRole('ADMIN')")
public class MessageApi {

    @Autowired
    private DateFormatterService dateFormatterService;
    @Autowired
    private SysMessageService sysMessageService;

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
}
