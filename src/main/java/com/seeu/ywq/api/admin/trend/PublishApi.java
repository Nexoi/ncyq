package com.seeu.ywq.api.admin.trend;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.PublishSRCTYPEConvertException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishAudio;
import com.seeu.ywq.trend.model.PublishVideo;
import com.seeu.ywq.trend.service.PublishAudioService;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.trend.service.PublishVideoService;
import com.seeu.ywq.user.service.UserPictureService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 22/01/2018
 * Time: 5:35 PM
 * Describe:
 */

@Api(tags = "动态-动态信息", description = "list/get/post/delete")
@RestController("adminPublishApi")
@RequestMapping("/api/admin/v1/publish")
public class PublishApi {

    @Autowired
    private PublishService publishService;
    @Resource
    private UserPictureService userPictureService;
    @Autowired
    private PublishVideoService publishVideoService;
    @Autowired
    private PublishAudioService publishAudioService;

    @ApiOperation("列表（默认按时间逆序）")
    @GetMapping("/list")
    public ResponseEntity list(@RequestParam(required = false) String word,
                               @RequestParam(required = false) PUBLISH search,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(required = false) String orderBy,
                               @RequestParam(required = false) Sort.Direction direction) {
        if (orderBy == null) orderBy = "createTime";
        if (direction == null) direction = Sort.Direction.DESC;
        PageRequest request = new PageRequest(page, size, new Sort(direction, orderBy));
        if (null == search || null == word) {
            return ResponseEntity.ok(publishService.findAll(request));
        } else {
            try {
                return ResponseEntity.ok(publishService.searchAll(search, word, request));
            } catch (ActionParameterException e) {
                return ResponseEntity.status(400).body(R.code(400).message("搜索 uid 或 id 时必须传入数字"));
            }
        }
    }

    @ApiOperation("获取一条动态详情")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(publishService.getOne(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该动态"));
        }
    }


    @ApiOperation("删除一条动态")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            publishService.deletePublish(id);
            return ResponseEntity.ok(R.code(200).message("删除成功！"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该动态"));
        }
    }

    /**
     * 发布新动态 (完全 copy 的用户组发布动态的行为)
     *
     * @param uid
     * @param publish
     * @param srcTypes
     * @param images
     * @return
     */
    @ApiOperation(value = "发布新动态（参考 APP 端发布动态的接口【保持一致】）", notes = "发布新动态，根据不同的动态类型（type字段）传入不同的数据")
    @ApiResponses({
            @ApiResponse(code = 201, message = "发布成功"),
            @ApiResponse(code = 400, message = "400 数据错误"),
            @ApiResponse(code = 500, message = "500 服务器异常，文件传输失败"),
    })
    @PostMapping
    public ResponseEntity add(@RequestParam(required = true) Long uid,
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
        publish.setUid(uid);
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
                    publish.setPictures(userPictureService.getPictureWithOutSave(uid, publish.getId(), album_types, images));  // 图片信息
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
                    publishVideo.setUid(uid);
                    // 必须提前持久化？no need
                    publish.setVideo(publishVideoService.save(publishVideo));
                    publish.setPictures(null);
                    publish.setAudio(null);
                    break;
                case audio:
                    PublishAudio audio = new PublishAudio();
                    audio.setAudioUrl(audioUrl);
                    audio.setCreateTime(new Date());
                    audio.setUid(uid);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.code(500).message("服务器异常，文件传输失败 :: " + e.getMessage()).build());
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
