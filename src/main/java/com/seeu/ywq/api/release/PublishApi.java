package com.seeu.ywq.api.release;

import com.seeu.core.R;
import com.seeu.ywq.release.dvo.PublishVO;
import com.seeu.ywq.release.model.Picture;
import com.seeu.ywq.release.model.Publish;
import com.seeu.ywq.release.model.PublishVideo;
import com.seeu.ywq.release.model.Video;
import com.seeu.ywq.release.repository.PublishRepository;
import com.seeu.ywq.release.repository.PublishVideoRepository;
import com.seeu.ywq.release.service.PublishService;
import com.seeu.ywq.release.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.*;
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

@Api(tags = {"动态"}, description = "发布新动态/查看动态", position = 10)
@RestController
@RequestMapping("/api/v1/publish")
public class PublishApi {
    @Resource
    private UserPictureService userPictureService;
    @Autowired
    private PublishService publishService;
    @Resource
    private PublishRepository publishRepository;
    @Resource
    private PublishVideoRepository publishVideoRepository;

    @ApiOperation(value = "获取某一条动态", notes = "根据发布动态ID获取动态内容")
    @ApiResponse(code = 404, message = "找不到该动态")
    @GetMapping("/{publishId}")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser, // 如果未登陆依然可以查看动态内容，但是内容可能会被限制
                              @PathVariable("publishId") Long publishId) {
        if (authUser == null) {
            PublishVO vo = publishService.viewIt(publishId);
            return (vo == null) ? ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build()) : ResponseEntity.ok(vo);
        } else {
            PublishVO vo = publishService.viewIt(publishId, authUser.getUid());
            return (vo == null) ? ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build()) : ResponseEntity.ok(vo);
        }
    }


    /**
     * 发布新动态
     *
     * @param authUser
     * @param publish
     * @param srcTypes
     * @param images
     * @return
     */
    @ApiOperation(value = "发布新动态", notes = "发布新动态，根据不同的动态类型（type字段）传入不同的数据")
    @ApiResponses({
            @ApiResponse(code = 201, message = "发布成功"),
            @ApiResponse(code = 400, message = "400 数据错误"),
            @ApiResponse(code = 500, message = "500 服务器异常，文件传输失败"),
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity add(@AuthenticationPrincipal UserLogin authUser,
                              Publish publish,
                              @ApiParam(value = "照片/视频类型，数组，用逗号隔开，可选值：open、close，分别表示：公开、私密。如：open,close,close", example = "open,close,open")
                                      Publish.SRC_TYPE[] srcTypes,// 照片类型（公开1/私密0）
                              MultipartFile[] images,
                              @RequestParam(required = false) String videoCoverUrl, // 视频封面
                              @RequestParam(required = false) String videoUrl//    视频链接
    ) {
        if (publish.getType() == null)
            return ResponseEntity.badRequest().body(R.code(4006).message("请选择一个动态类型").build());
        if (publish.getType() != Publish.PUBLISH_TYPE.word && srcTypes == null) {
            return ResponseEntity.badRequest().body(R.code(4007).message("动态类型 [" + publish.getType().name() + "] 需要同时上传对应的资源类型字段：srcTypes").build());
        }
        // if picture
        if (publish.getType() == Publish.PUBLISH_TYPE.picture) {
            if (images == null || images.length == 0)
                return ResponseEntity.badRequest().body(R.code(4001).message("请传入至少一张图片").build());
            if (images.length != srcTypes.length)
                return ResponseEntity.badRequest().body(R.code(4002).message("参数错误，srcTypes 长度需要和 images 一致").build());
        }
        // if video
        if (publish.getType() == Publish.PUBLISH_TYPE.video) {
            if (videoCoverUrl == null || videoUrl == null)
                return ResponseEntity.badRequest().body(R.code(4005).message("视频内容不能为空").build());
            if (srcTypes.length != 1)
                return ResponseEntity.badRequest().body(R.code(4006).message("视频类型（公开/私密）数组长度必须为 1").build());
            Video video = new Video();
            video.setCoverUrl(videoCoverUrl);
            video.setSrcUrl(videoUrl);
            PublishVideo publishVideo = new PublishVideo();
            publishVideo.setVideo(video);
            publishVideo.setVideoType(srcTypes[0] == Publish.SRC_TYPE.open ? PublishVideo.VIDEO_TYPE.open : PublishVideo.VIDEO_TYPE.close);
            publishVideo.setCreateTime(new Date());
            publishVideo.setDeleteFlag(PublishVideo.DELETE_FLAG.show);
            publishVideo.setUid(authUser.getUid());
            // 必须提前持久化？no need
//            PublishVideo savedPublishVideo = publishVideoRepository.save(publishVideo);
            publish.setVideo(publishVideo);
        }
        // 初始化判断
        if (publish.getTitle() == null || publish.getTitle().trim().length() == 0)
            return ResponseEntity.badRequest().body(R.code(4003).message("标题内容不能为空").build());
        if (publish.getText() == null && Publish.PUBLISH_TYPE.word == publish.getType())
            return ResponseEntity.badRequest().body(R.code(4004).message("文本内容不能为空").build());

        // 数据规整
        publish.setId(null);
        publish.setUid(authUser.getUid());
        publish.setCommentNum(0);
        publish.setLikedUsers(null);
        publish.setLikeNum(0);
        publish.setViewNum(0);
        publish.setStatus(Publish.STATUS.normal); // 初始化为正常
        publish.setUnlockPrice(publish.getUnlockPrice() == null ? BigDecimal.ZERO : publish.getUnlockPrice());
        publish.setCreateTime(new Date());
        publish.setType(publish.getType());
        try {
            switch (publish.getType()) {
                case word:
                    publish.setUnlockPrice(BigDecimal.ZERO);
                    publish.setPictures(null);
                    publish.setVideo(null);
                    break;
                case video:
                    publish.setPictures(null);
                    break;
                case picture:
                    Picture.ALBUM_TYPE[] album_types = new Picture.ALBUM_TYPE[srcTypes.length];
                    for (int i = 0; i < srcTypes.length; i++) {
                        album_types[i] = srcTypes[i] == Publish.SRC_TYPE.open ? Picture.ALBUM_TYPE.open : Picture.ALBUM_TYPE.close;
                    }
                    publish.setPictures(userPictureService.getPictureWithOutSave(authUser.getUid(), publish.getId(), album_types, images));  // 图片信息
                    publish.setVideo(null);
                    break;
            }
            // 发布信息持久化
            Publish p = publishRepository.save(publish);
            return ResponseEntity.status(201).body(publishService.transferToVO(p, authUser.getUid()));
        } catch (Exception e) {
            // 注意回滚（如果异常，阿里云可能会存储部分图片，但本地可能无对应图片信息）
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.code(500).message("服务器异常，文件传输失败").build());
        }
    }

    @ApiOperation(value = "删除某一条动态【本人】", notes = "根据发布动态ID删除动态")
    @DeleteMapping("/{publishId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity delete(@AuthenticationPrincipal UserLogin authUser,
                                 @PathVariable("publishId") Long publishId) {
        Publish publish = publishRepository.findByIdAndUidAndStatus(publishId, authUser.getUid(), Publish.STATUS.normal);
        if (publish == null) {
            return ResponseEntity.status(404).body(R.code(404).message("您无此动态信息").build());
        }
        if (!publish.getUid().equals(authUser.getUid())) {
            return ResponseEntity.badRequest().body(R.code(400).message("不能删除非本人的动态信息").build());
        }
        // 会一并清除点赞、评论等信息
        publishService.deletePublish(publishId);
        return ResponseEntity.ok().build();
    }
}
