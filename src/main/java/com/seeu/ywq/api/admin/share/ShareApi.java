package com.seeu.ywq.api.admin.share;

import com.seeu.core.R;
import com.seeu.ywq.share.model.SharePicture;
import com.seeu.ywq.share.service.SharePicturesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 31/01/2018
 * Time: 6:30 PM
 * Describe:
 */


@Api(tags = "配置-分享", description = "分享图片")
@RestController("adminShareApi")
@RequestMapping("/api/admin/v1/share")
public class ShareApi {

    @Autowired
    private SharePicturesService sharePicturesService;

    @ApiOperation("列出所有图片")
    @GetMapping("/pictures")
    public List<SharePicture> list() {
        return sharePicturesService.findAll();
    }

    @ApiOperation("添加图片")
    @PostMapping("/picture")
    public SharePicture add(String imgUrl) {
        SharePicture picture = new SharePicture();
        picture.setImgUrl(imgUrl);
        picture.setCreateTime(new Date());
        return sharePicturesService.save(picture);
    }

    @DeleteMapping("/picture/{id}")
    public R.ResponseBuilder delete(@PathVariable Long id) {
        sharePicturesService.delete(id);
        return R.code(200).message("删除成功");
    }
}
