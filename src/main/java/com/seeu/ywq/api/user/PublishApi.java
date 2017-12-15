package com.seeu.ywq.api.user;

import com.seeu.ywq.user.model.Picture;
import com.seeu.ywq.user.model.Publish;
import com.seeu.ywq.user.repository.PublishRepository;
import com.seeu.ywq.user.service.PublishService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/publish")
public class PublishApi {
    @Resource
    private UserPictureService userPictureService;
    @Autowired
    private PublishService publishService;
    @Resource
    private PublishRepository publishRepository;

    @GetMapping("/{publishId}")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser, // 如果未登陆依然可以查看动态内容，但是内容可能会被限制
                              @PathVariable("publishId") Long publishId) {
        if (authUser == null) {
            return ResponseEntity.ok(publishService.findOneByPublishId(publishId));
        } else {
            return ResponseEntity.ok(publishService.findOneByPublishId(publishId, authUser.getUid()));
        }
    }


    /**
     * 发布新动态
     *
     * @param authUser
     * @param publish
     * @param pictureType
     * @param images
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "发布成功"),
            @ApiResponse(code = 4001, message = "动态类型为“图片”，请传入至少一张图片"),
            @ApiResponse(code = 4002, message = "参数错误，pictureType 长度需要和 images 一致"),
            @ApiResponse(code = 4003, message = "标题内容不能为空"),
            @ApiResponse(code = 4004, message = "动态类型为“文字”，文本内容不能为空"),
            @ApiResponse(code = 500, message = "服务器异常，文件传输失败"),
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity add(@AuthenticationPrincipal UserLogin authUser,
                              Publish publish,
                              Picture.ALBUM_TYPE[] pictureType,// 照片类型（公开1/私密0）
                              MultipartFile[] images) {
        if (publish.getType() != null && publish.getType() == Publish.PUBLISH_TYPE.picture) {
            if (images == null || images.length == 0)
                return ResponseEntity.status(4001).body("请传入至少一张图片");
            if (images.length != pictureType.length)
                return ResponseEntity.status(4002).body("参数错误，pictureType 长度需要和 images 一致");
        }
        // 初始化判断
        if (publish.getTitle() == null || publish.getTitle().trim().length() == 0)
            return ResponseEntity.status(4003).body("标题内容不能为空");
        if (publish.getText() == null && Publish.PUBLISH_TYPE.word == publish.getType())
            return ResponseEntity.status(4004).body("文本内容不能为空");

        // 数据规整
        publish.setId(null);
        publish.setUid(authUser.getUid());
        publish.setCommentNum(0);
        publish.setLikedUserList(null);
        publish.setLikeNum(0);
        publish.setViewNum(0);
        publish.setState(Publish.PUBLIC_STATUS.normal); // 初始化为正常
        publish.setUnlockPrice(publish.getUnlockPrice() == null ? BigDecimal.ZERO : publish.getUnlockPrice());
        publish.setCreateTime(new Date());
        publish.setType(publish.getType() == null ? Publish.PUBLISH_TYPE.word : publish.getType());
        try {
            switch (publish.getType()) {
                case word:
                    publish.setPictures(null);
                    publish.setUnlockPrice(BigDecimal.ZERO);
                    publish.setVideoUrls(null);
                    publish.setCoverVideoUrl(null);
                    break;
                case video:
                    publish.setPictures(null);
                    break;
                case picture:
                    publish.setPictures(userPictureService.getPictureWithOutSave(authUser.getUid(), publish.getId(), pictureType, images));  // 图片信息
                    publish.setVideoUrls(null);
                    publish.setCoverVideoUrl(null);
                    break;
                default:
                    publish.setPictures(userPictureService.getPictureWithOutSave(authUser.getUid(), publish.getId(), pictureType, images));  // 图片信息
                    break;
            }
            // 发布信息持久化
            return ResponseEntity.ok(publishService.transferToVO(publishRepository.save(publish)));
        } catch (Exception e) {
            // 注意回滚（如果异常，阿里云可能会存储部分图片，但本地可能无对应图片信息）
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器异常，文件传输失败");
        }
    }
}
