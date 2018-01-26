package com.seeu.ywq.api.release.trend;

import com.seeu.core.R;
import com.seeu.ywq.exception.PublishSRCTYPEConvertException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.dvo.PublishVO;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishAudio;
import com.seeu.ywq.trend.model.PublishVideo;
import com.seeu.ywq.trend.service.PublishAudioService;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.trend.service.PublishVideoService;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

@Api(tags = {"动态"}, description = "发布新动态/查看动态", position = 10)
@RestController
@RequestMapping("/api/v1/publish")
public class PublishApi {
    @Resource
    private UserPictureService userPictureService;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private PublishVideoService publishVideoService;
    @Autowired
    private PublishAudioService publishAudioService;

    @ApiOperation(value = "获取某一条动态", notes = "根据发布动态ID获取动态内容")
    @ApiResponse(code = 404, message = "找不到该动态")
    @GetMapping("/{publishId}")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser, // 如果未登陆依然可以查看动态内容，但是内容可能会被限制
                              @PathVariable("publishId") Long publishId) {
        if (publishId <= 0)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build());
        if (authUser == null) {
            PublishVO vo = publishService.viewIt(publishId);
            if (vo == null) return ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build());
            SimpleUserVO userVO = userReactService.findOneAndTransferToVO(null, vo.getUid());
            vo.setUser(userVO);
            if (userVO != null) vo.setUid(null);
            return ResponseEntity.ok(vo);
        } else {
            PublishVO vo = publishService.viewIt(publishId, authUser.getUid());
            if (vo == null) return ResponseEntity.status(404).body(R.code(404).message("找不到该动态").build());
            SimpleUserVO userVO = userReactService.findOneAndTransferToVO(authUser.getUid(), vo.getUid());
            vo.setUser(userVO);
            if (userVO != null) vo.setUid(null);
            return ResponseEntity.ok(vo);
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
    // Publish.SRC_TYPE[] srcTypes,// 照片类型（公开1/私密0）[废弃该参数]
    public ResponseEntity add(@AuthenticationPrincipal UserLogin authUser,
                              Publish publish,
                              @ApiParam(value = "照片/视频类型，数组，用逗号隔开，可选值：open、close，分别表示：公开、私密。如：open,close,close", example = "open,close,open")
                              @RequestParam(required = false) String srcTypes,
                              MultipartFile[] images,
                              @RequestParam(required = false) Integer videoWidth,
                              @RequestParam(required = false) Integer videoHeight,
                              @RequestParam(required = false) String videoCoverUrl, // 视频封面
                              @RequestParam(required = false) String videoUrl,//    视频链接
                              @RequestParam(required = false) String audioUrl,   // audio link
                              @RequestParam(required = false) Long audioSecond
    ) {
        // 数据规整
        publish.setId(null);
        publish.setUid(authUser.getUid());
        publish.setCommentNum(0);
        publish.setLikedUsers(null);
        publish.setLikeNum(0);
        publish.setViewNum(0);
        publish.setReceivedDiamonds(0L);
        publish.setStatus(Publish.STATUS.normal); // 初始化为正常
        publish.setUnlockPrice(publish.getUnlockPrice() == null ? 0L : publish.getUnlockPrice());
        publish.setCreateTime(new Date());
        publish.setType(publish.getType());
        // 初始化判断
//        if (publish.getTitle() == null || publish.getTitle().trim().length() == 0)
//            return ResponseEntity.badRequest().body(R.code(4003).message("标题内容不能为空").build());
//        if (publish.getText() == null && Publish.PUBLISH_TYPE.word == publish.getType())
//            return ResponseEntity.badRequest().body(R.code(4004).message("文本内容不能为空").build());

        //
        if (publish.getType() == null)
            return ResponseEntity.badRequest().body(R.code(4006).message("请选择一个动态类型").build());
        try {
            switch (publish.getType()) {
                case word:
                    publish.setUnlockPrice(0L);
                    publish.setPictures(null);
                    publish.setVideo(null);
                    publish.setAudio(null);
                    break;
                case picture:
                    Picture.ALBUM_TYPE[] album_types = null;
                    try {
                        album_types = formPictureTypes(srcTypes);
                    } catch (PublishSRCTYPEConvertException e) {
                        return ResponseEntity.badRequest().body(R.code(4006).message("动态类型 [" + publish.getType().name() + "] 资源类型字段：srcTypes 解析错误").build());
                    }
                    // check length
                    if (images == null || images.length == 0)
                        return ResponseEntity.badRequest().body(R.code(4001).message("请传入至少一张图片").build());
                    if (images.length != album_types.length)
                        return ResponseEntity.badRequest().body(R.code(4002).message("参数错误，srcTypes 长度需要和 images 一致").build());
                    publish.setPictures(userPictureService.getPictureWithOutSave(authUser.getUid(), publish.getId(), album_types, images));  // 图片信息
                    publish.setVideo(null);
                    publish.setAudio(null);
                    break;
                case video:
                    if (videoCoverUrl == null || videoUrl == null)
                        return ResponseEntity.badRequest().body(R.code(4005).message("视频内容不能为空").build());
                    if (srcTypes == null || !"open".equals(srcTypes) && !"close".equals(srcTypes))
                        return ResponseEntity.badRequest().body(R.code(4006).message("视频类型（公开/私密）字段 srcTypes 解析错误").build());
                    Video video = new Video();
                    video.setHeight(videoHeight);
                    video.setWidth(videoWidth);
                    video.setCoverUrl(videoCoverUrl);
                    video.setSrcUrl(videoUrl);
                    video.setId(null);
                    PublishVideo publishVideo = new PublishVideo();
                    publishVideo.setVideo(video);
                    publishVideo.setVideoType("open".equals(srcTypes) ? PublishVideo.VIDEO_TYPE.open : PublishVideo.VIDEO_TYPE.close);
                    publishVideo.setCreateTime(new Date());
                    publishVideo.setDeleteFlag(PublishVideo.DELETE_FLAG.show);
                    publishVideo.setUid(authUser.getUid());
                    // 必须提前持久化？no need
                    publish.setVideo(publishVideoService.save(publishVideo));
                    publish.setPictures(null);
                    publish.setAudio(null);
                    break;
                case audio:
                    PublishAudio audio = new PublishAudio();
                    audio.setAudioUrl(audioUrl);
                    audio.setCreateTime(new Date());
                    audio.setUid(authUser.getUid());
                    audio.setAudioSecond(audioSecond == null ? 0L : audioSecond);
                    audio.setDeleteFlag(PublishAudio.DELETE_FLAG.show);
                    publish.setAudio(publishAudioService.save(audio));
                    publish.setPictures(null);
                    publish.setVideo(null);
                    break;
            }
            // 发布信息持久化
            publishService.save(publish);
            return ResponseEntity.status(201).body(R.code(201).message("publish success"));
        } catch (Exception e) {
            // 注意回滚（如果异常，阿里云可能会存储部分图片，但本地可能无对应图片信息）
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.code(500).message("服务器异常，文件传输失败").build());
        }
    }

//    /**
//     * 发布新动态
//     *
//     * @param authUser
//     * @param publish
//     * @param srcTypes
//     * @param picturesDTO
//     * @return
//     */
//    @Deprecated
//    @ApiOperation(value = "发布新动态（图片预上传）", notes = "发布新动态，根据不同的动态类型（type字段）传入不同的数据", hidden = true)
//    @ApiResponses({
//            @ApiResponse(code = 201, message = "发布成功"),
//            @ApiResponse(code = 400, message = "400 数据错误"),
//            @ApiResponse(code = 500, message = "500 服务器异常，文件传输失败"),
//    })
//    @PostMapping("/with-url")
//    @PreAuthorize("hasRole('USER')")
//    // Publish.SRC_TYPE[] srcTypes,// 照片类型（公开1/私密0）[废弃该参数]
//    public ResponseEntity addWithJSON(@AuthenticationPrincipal UserLogin authUser,
//                                      Publish publish,
//                                      @Validated PicturesDTO picturesDTO,
//                                      @ApiParam(value = "照片/视频类型，数组，用逗号隔开，可选值：open、close，分别表示：公开、私密。如：open,close,close", example = "open,close,open")
//                                      @RequestParam(required = false) String srcTypes,
//                                      @RequestParam(required = false) String videoCoverUrl, // 视频封面
//                                      @RequestParam(required = false) String videoUrl//    视频链接
//    ) {
//        Publish.SRC_TYPE[] srcTypesArray = null;
//        try {
//            srcTypesArray = formTypes(srcTypes);
//        } catch (PublishSRCTYPEConvertException e) {
//            return ResponseEntity.badRequest().body(R.code(4008).message("动态类型 [" + publish.getType().name() + "] 资源类型字段：srcTypes 解析错误").build());
//        }
//        if (srcTypes == null)
//            if (publish.getType() == null)
//                return ResponseEntity.badRequest().body(R.code(4006).message("请选择一个动态类型").build());
//        if (publish.getType() != Publish.PUBLISH_TYPE.word && publish.getType() != Publish.PUBLISH_TYPE.picture && srcTypesArray == null) {
//            return ResponseEntity.badRequest().body(R.code(4007).message("动态类型 [" + publish.getType().name() + "] 需要同时上传对应的资源类型字段：srcTypes").build());
//        }
//        // if picture
//        if (publish.getType() == Publish.PUBLISH_TYPE.picture) {
//            if (picturesDTO.getPictures() == null || picturesDTO.getPictures().size() == 0)
//                return ResponseEntity.badRequest().body(R.code(4001).message("请传入至少一张图片").build());
//        }
//        // if video
//        if (publish.getType() == Publish.PUBLISH_TYPE.video) {
//
//            if (videoCoverUrl == null || videoUrl == null)
//                return ResponseEntity.badRequest().body(R.code(4005).message("视频内容不能为空").build());
//            if (srcTypesArray.length != 1)
//                return ResponseEntity.badRequest().body(R.code(4006).message("视频类型（公开/私密）数组长度必须为 1").build());
//            Video video = new Video();
//            video.setCoverUrl(videoCoverUrl);
//            video.setSrcUrl(videoUrl);
//            video.setId(null);
//            PublishVideo publishVideo = new PublishVideo();
//            publishVideo.setVideo(video);
//            publishVideo.setVideoType(srcTypesArray[0] == Publish.SRC_TYPE.open ? PublishVideo.VIDEO_TYPE.open : PublishVideo.VIDEO_TYPE.close);
//            publishVideo.setCreateTime(new Date());
//            publishVideo.setDeleteFlag(PublishVideo.DELETE_FLAG.show);
//            publishVideo.setUid(authUser.getUid());
//            // 必须提前持久化？no need
//            publishVideo = publishVideoService.save(publishVideo);
//            publish.setVideo(publishVideo);
//        }
//        // 初始化判断
//        if (publish.getTitle() == null || publish.getTitle().trim().length() == 0)
//            return ResponseEntity.badRequest().body(R.code(4003).message("标题内容不能为空").build());
//        if (publish.getText() == null && Publish.PUBLISH_TYPE.word == publish.getType())
//            return ResponseEntity.badRequest().body(R.code(4004).message("文本内容不能为空").build());
//
//        // 数据规整
//        publish.setId(null);
//        publish.setUid(authUser.getUid());
//        publish.setCommentNum(0);
//        publish.setLikedUsers(null);
//        publish.setLikeNum(0);
//        publish.setViewNum(0);
//        publish.setReceivedDiamonds(0L);
//        publish.setStatus(Publish.STATUS.normal); // 初始化为正常
//        publish.setUnlockPrice(publish.getUnlockPrice() == null ? 0l : publish.getUnlockPrice());
//        publish.setCreateTime(new Date());
//        publish.setType(publish.getType());
//        try {
//            switch (publish.getType()) {
//                case word:
//                    publish.setUnlockPrice(0l);
//                    publish.setPictures(null);
//                    publish.setVideo(null);
//                    break;
//                case video:
//                    publish.setPictures(null);
//                    break;
//                case picture:
//                    // 添加 publish 信息
//                    List<Picture> pictureList = new ArrayList<>();
//                    for (PictureDTO pictureDTO : picturesDTO.getPictures()) {
//                        Picture picture = new Picture();
//                        BeanUtils.copyProperties(pictureDTO, picture);
//                        picture.setDeleteFlag(Picture.DELETE_FLAG.show);
//                        picture.setCreateTime(new Date());
//                        picture.setUid(authUser.getUid());
//                        picture.setPublishId(publish.getId()); //?null? （清空，怕用户上传 id 就不好玩了）
//                        // 图片 open
//                        Image imageOpen = new Image();
//                        BeanUtils.copyProperties(pictureDTO.getImageOpen(), imageOpen);
//                        imageOpen.setCreateDate(new Date());
//                        picture.setImageOpen(imageOpen);
//                        picture.setImageClose(null);
//                        // 图片 close
//                        if (picture.getAlbumType() == Picture.ALBUM_TYPE.close) {
//                            if (pictureDTO.getImageClose() == null)
//                                return ResponseEntity.badRequest().body(R.code(4010).message("上传隐私图片必须至少包含完整的图片信息"));
//                            Image imageClose = new Image();
//                            BeanUtils.copyProperties(pictureDTO.getImageClose(), imageClose);
//                            imageClose.setCreateDate(new Date());
//                            picture.setImageOpen(imageClose);
//                        }
//                        pictureList.add(picture);
//                    }
//                    publish.setPictures(pictureList);  // 图片信息
//                    publish.setVideo(null);
//                    break;
//            }
//            // 发布信息持久化
//            Publish p = publishService.saveWithImage(publish);
//            return ResponseEntity.status(201).body(publishService.viewIt(p.getId(), authUser.getUid()));
//        } catch (Exception e) {
//            // 注意回滚（如果异常，阿里云可能会存储部分图片，但本地可能无对应图片信息）
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.code(500).message("服务器异常，文件传输失败").build());
//        }
//    }

//    @ApiOperation(value = "发布动态前上传图片", notes = "需要一张张上传，并附带 open/close 信息")
//    @PostMapping("/upload-image")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity uploadImageOneByOne(@RequestParam(required = true) MultipartFile image,
//                                              @RequestParam(required = true) Publish.SRC_TYPE type) {
//        try {
//            if (type == Publish.SRC_TYPE.open) {
//                return ResponseEntity.ok(fileUploadService.uploadImageWithPictureType(image, Picture.ALBUM_TYPE.open));
//            } else if (type == Publish.SRC_TYPE.close)
//                return ResponseEntity.ok(fileUploadService.uploadImageWithPictureType(image, Picture.ALBUM_TYPE.close));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(500).body(R.code(500).message("服务器异常，上传失败"));
//    }


    @ApiOperation(value = "删除某一条动态【本人】", notes = "根据发布动态ID删除动态")
    @DeleteMapping("/{publishId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity delete(@AuthenticationPrincipal UserLogin authUser,
                                 @PathVariable("publishId") Long publishId) {
        Publish publish = publishService.findOne(authUser.getUid(), publishId);
        if (publish == null) {
            return ResponseEntity.status(404).body(R.code(404).message("您无此动态信息").build());
        }
        if (!publish.getUid().equals(authUser.getUid())) {
            return ResponseEntity.badRequest().body(R.code(400).message("不能删除非本人的动态信息").build());
        }
        // 会一并清除点赞、评论等信息
        try {
            publishService.deletePublish(publishId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("您无此动态信息").build());
        }
        return ResponseEntity.ok().build();
    }


    private Publish.SRC_TYPE[] formTypes(String string) throws PublishSRCTYPEConvertException {
        if (string == null) return null;
        try {
            String[] arr = string.split(",");
            Publish.SRC_TYPE[] types = new Publish.SRC_TYPE[arr.length];
            for (int i = 0; i < arr.length; i++) {
                String type = arr[i];
                if (type == null || (!type.equals("open") && !type.equals("close")))
                    throw new PublishSRCTYPEConvertException();
                types[i] = type.equals("open") ? Publish.SRC_TYPE.open : Publish.SRC_TYPE.close;
            }
            return types;
        } catch (Exception e) {
            throw new PublishSRCTYPEConvertException();
        }
    }

    private Picture.ALBUM_TYPE[] formPictureTypes(String string) throws PublishSRCTYPEConvertException {
        if (string == null) return new Picture.ALBUM_TYPE[0];
        try {
            String[] arr = string.split(",");
            Picture.ALBUM_TYPE[] types = new Picture.ALBUM_TYPE[arr.length];
            for (int i = 0; i < arr.length; i++) {
                String type = arr[i];
                if (type == null || (!type.equals("open") && !type.equals("close")))
                    throw new PublishSRCTYPEConvertException();
                types[i] = type.equals("open") ? Picture.ALBUM_TYPE.open : Picture.ALBUM_TYPE.close;
            }
            return types;
        } catch (Exception e) {
            throw new PublishSRCTYPEConvertException();
        }
    }
}
