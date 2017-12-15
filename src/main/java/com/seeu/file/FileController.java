package com.seeu.file;


import com.alibaba.fastjson.JSONObject;
import com.seeu.file.storage.StorageService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by neo on 19/01/2017.
 * <p>
 * mappping 的 /data 表示不同的物理存储仓库，此处为本地 API 服务器，将来扩展仓库的时候，对应 upload 接口 里的仓库名字也应该更改
 */
@RestController
@Api(value = "文件上传")
public class FileController {
    private final StorageService storageService;

    @Autowired
    FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/data/**")
    public ResponseEntity<?> loadFile(HttpServletRequest request) {
        String filePath = request.getRequestURI().substring(6);// 去掉 '/data/' 这 6 个字符
        Resource resource = storageService.loadAsResource(filePath);
        return ResponseEntity.ok().body(resource);
    }

    @ApiOperation(value = "文件上传", notes = "只需要传入 file 参数即可，其余参数为用户权限，会自动注入，手动填写视为无效")
    @ApiResponse(code = 200, message = "上传成功，返回信息包含文件 Url")
    @PostMapping("/api/v1/upload")
    @PreAuthorize("hasRole('USER')") // 注册用户才能使用
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @ApiParam(name = "注册用户才拥有权限上传文件，参数不需要手动填写")
                                        @AuthenticationPrincipal UserLogin user) {
        String name = storageService.store(file);
        JSONObject json = new JSONObject();
        json.put("filename", "/data/" + name);
        json.put("authName", user.getUsername());
        json.put("uploadTime", new Date().toString());
        return ResponseEntity.ok(json);
    }

}
