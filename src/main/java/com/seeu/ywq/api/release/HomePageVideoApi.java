package com.seeu.ywq.api.release;

import com.seeu.core.R;
import com.seeu.ywq.release.dvo.apppage.SimpleUserVO;
import com.seeu.ywq.release.model.FansPKeys;
import com.seeu.ywq.release.model.apppage.HomePageVideo;
import com.seeu.ywq.release.model.apppage.HomePageVideoComment;
import com.seeu.ywq.release.repository.FansRepository;
import com.seeu.ywq.release.repository.apppage.HomePageVideoCommentRepository;
import com.seeu.ywq.release.repository.apppage.HomePageVideoRepository;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "视频+评论（管理员置入的视频）", description = "获取视频详情信息/评论视频")
@RestController
@RequestMapping("/api/v1/video")
public class HomePageVideoApi {

    @Resource
    private HomePageVideoRepository homePageVideoRepository;
    @Resource
    private UserLoginRepository userLoginRepository;
    @Resource
    private FansRepository fansRepository;
    @Resource
    private HomePageVideoCommentRepository homePageVideoCommentRepository;

    @ApiOperation(value = "查看视频详情", notes = "推荐视频列表按时间最新排序；评论列表按时间最新排序")
    @ApiResponses({
            @ApiResponse(code = 404, message = "找不到该视频")
    })
    @GetMapping("/{videoId}")
    public ResponseEntity get(@AuthenticationPrincipal UserLogin authUser,
                              @PathVariable Long videoId,
                              @ApiParam(value = "详情页视频推荐页码，默认第 0 页")
                              @RequestParam(defaultValue = "0") Integer suggestPage,
                              @ApiParam(value = "详情页视频推荐每页数量，默认 6 条")
                              @RequestParam(defaultValue = "6") Integer suggestSize,
                              @ApiParam(value = "详情页评论页码，默认第 0 页")
                              @RequestParam(defaultValue = "0") Integer commentPage,
                              @ApiParam(value = "详情页评论每页数量，默认 10 条")
                              @RequestParam(defaultValue = "6") Integer commentSize
    ) {
        HomePageVideo video = homePageVideoRepository.findOne(videoId);
        if (video == null || video.getDeleteFlag() != HomePageVideo.DELETE_FLAG.show)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该视频").build());
        // 浏览一次
        homePageVideoRepository.viewItOnce(videoId);
        // 注入发布者用户信息
        Long uid = video.getUid();
        UserLogin ul = userLoginRepository.findOne(uid);
        SimpleUserVO userVO = new SimpleUserVO();
        BeanUtils.copyProperties(ul, userVO);   // if ul==null，则默认设定为空
        userVO.setFollowed(false); // 默认关注为 false
        // 检查关注情况
        if (authUser != null) {
            userVO.setFollowed(fansRepository.exists(new FansPKeys(authUser.getUid(), uid)));
        }
        // 查看发布者最近发布的视频信息（按时间最新排序）
        Page page = homePageVideoRepository.findAllByUid(uid, new PageRequest(suggestPage, suggestSize, new Sort(Sort.Direction.DESC, "createTime")));
        // 评论信息（按时间最新排序）
        Page comment_page = homePageVideoCommentRepository.findAllByVideoIdAndDeleteFlagAndFatherIdIsNull(videoId, HomePageVideoComment.DELETE_FLAG.show, new PageRequest(commentPage, commentSize, new Sort(Sort.Direction.DESC, "commentDate")));
        Map map = new HashMap();
        map.put("video", video);
        map.put("user", userVO);
        map.put("videoSuggestion", page);
        map.put("comments", comment_page);
        return ResponseEntity.ok(map);
    }


    @ApiOperation(value = "获取评论", notes = "获取某视频下的评论信息【分页】")
    @GetMapping("/{videoId}/comment")
    public ResponseEntity getComments(@PathVariable Long videoId,
                                      @ApiParam(value = "详情页评论页码，默认第 0 页")
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @ApiParam(value = "详情页评论每页数量，默认 6 条")
                                      @RequestParam(defaultValue = "6") Integer size) {
        Page comment_page = homePageVideoCommentRepository.findAllByVideoIdAndDeleteFlagAndFatherIdIsNull(videoId, HomePageVideoComment.DELETE_FLAG.show, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "commentDate")));
        return ResponseEntity.ok(comment_page);
    }

    @ApiOperation(value = "添加评论", notes = "添加一条新的评论信息")
    @PostMapping("/{videoId}/comment")
    public ResponseEntity comment(@AuthenticationPrincipal UserLogin authUser, @PathVariable Long videoId, String text) {
        HomePageVideoComment comment = new HomePageVideoComment();
        comment.setCommentDate(new Date());
        comment.setHeadIconUrl(authUser.getHeadIconUrl());
        comment.setText(text);
        comment.setUid(authUser.getUid());
        comment.setUsername(authUser.getNickname());
        comment.setVideoId(videoId);
        comment = homePageVideoCommentRepository.save(comment);
        return ResponseEntity.status(201).body(comment);
    }

    @ApiOperation(value = "添加评论回复", notes = "添加一条评论信息的回复内容")
    @PostMapping("/{videoId}/comment/{fatherId}")
    public ResponseEntity commentReply(@AuthenticationPrincipal UserLogin authUser,
                                       @PathVariable Long videoId,
                                       @ApiParam(value = "父评论ID，回复的内容会挂在这条评论下")
                                       @PathVariable Long fatherId,
                                       String text) {
        HomePageVideoComment comment = new HomePageVideoComment();
        comment.setCommentDate(new Date());
        comment.setHeadIconUrl(authUser.getHeadIconUrl());
        comment.setText(text);
        comment.setUid(authUser.getUid());
        comment.setUsername(authUser.getNickname());
        comment.setVideoId(videoId);
        comment.setFatherId(fatherId);
        comment = homePageVideoCommentRepository.save(comment);
        return ResponseEntity.status(201).body(comment);
    }

    @ApiOperation(value = "删除评论", notes = "删除该评论信息")
    @DeleteMapping("/{videoId}/comment/{commentId}")
    public ResponseEntity deleteComment(@AuthenticationPrincipal UserLogin authUser,
                                        @PathVariable Long videoId,
                                        @PathVariable Long commentId) {
        HomePageVideoComment comment = homePageVideoCommentRepository.findOne(commentId);
        if (comment == null || comment.getDeleteFlag() != HomePageVideoComment.DELETE_FLAG.show)
            return ResponseEntity.status(404).body(R.code(404).message("找不到该评论").build());
        if (!authUser.getUid().equals(comment.getUid()))
            return ResponseEntity.badRequest().body(R.code(4000).message("不可以删除非自己评论的内容").build());
//        if (!videoId.equals(comment.getVideoId()))
//            return ResponseEntity.badRequest().body(R.code(4001).message("该评论信息和视频不对应，无法删除").build());
        // 软删除（deleteFlag）
        comment.setDeleteFlag(HomePageVideoComment.DELETE_FLAG.delete);
        homePageVideoCommentRepository.save(comment);
        return ResponseEntity.ok().body(R.code(200).message("删除成功！").build());
    }
}
