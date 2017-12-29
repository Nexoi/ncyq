package com.seeu.ywq.api.release;

import com.seeu.core.R;
import com.seeu.ywq.release.service.PublishService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 动态内容交互API
 */
@Api(tags = "动态内容互动", description = "点赞、评论等")
@RestController
@RequestMapping("/api/v1/publish")
public class PublishOperationApi {

    @Autowired
    private PublishService publishService;

    @ApiOperation(value = "点赞列表", notes = "获取点赞信息")
    @GetMapping("/{publishId}/like")
    public ResponseEntity listLikes(@PathVariable("publishId") Long publishId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(publishService.listLikedUser(publishId, new PageRequest(page, size)));
    }

    @ApiOperation(value = "点赞【登录】", notes = "对某一条动态进行点赞")
    @ApiResponses({
            @ApiResponse(code = 200, message = "点赞成功"),
            @ApiResponse(code = 400, message = "错误，您已经点过赞了"),
            @ApiResponse(code = 404, message = "找不到该动态，无法点赞")
    })
    @PostMapping("/{publishId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity likeIt(@AuthenticationPrincipal UserLogin authUser,
                                 @PathVariable("publishId") Long publishId) {

        PublishService.STATUS status = publishService.likeIt(publishId, authUser);
        switch (status) {
            case success:
                return ResponseEntity.ok().body(R.code(200).message("点赞成功").build());
            case not_found_publish:
                return ResponseEntity.status(404).body(R.code(404).message("找不到该动态，无法点赞").build());
            case existed_not_modify:
                return ResponseEntity.status(400).body(R.code(400).message("您已经点过赞了").build());
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }

    @ApiOperation(value = "取消点赞【登录】", notes = "对某一条动态进行取消点赞，必须是已登陆用户且对该动态点过赞")
    @ApiResponses({
            @ApiResponse(code = 200, message = "取消点赞成功"),
            @ApiResponse(code = 400, message = "错误，您未对此动态点过赞"),
            @ApiResponse(code = 404, message = "找不到该动态，无法取消点赞")
    })
    @DeleteMapping("/{publishId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity dislikeIt(@AuthenticationPrincipal UserLogin authUser,
                                    @PathVariable("publishId") Long publishId) {
        PublishService.STATUS status = publishService.dislikeIt(publishId, authUser.getUid());
        switch (status) {
            case success:
                return ResponseEntity.ok().body(R.code(200).message("取消点赞成功").build());
            case not_found_publish:
                return ResponseEntity.status(404).body(R.code(404).message("找不到该动态，无法取消点赞").build());
            case not_existed:
                return ResponseEntity.status(400).body(R.code(400).message("您未对此动态点过赞").build());
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }

    @ApiOperation(value = "评论列表", notes = "获取评论信息")
    @GetMapping("/{publishId}/comment")
    public ResponseEntity listComments(@PathVariable("publishId") Long publishId,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(publishService.listComments(publishId, new PageRequest(page, size)));
    }

    @ApiOperation(value = "评论【登录】", notes = "对某一条动态进行评论，必须是已登陆用户")
    @ApiResponses({
            @ApiResponse(code = 201, message = "评论成功"),
            @ApiResponse(code = 404, message = "找不到该动态，无法评论")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{publishId}/comment")
    public ResponseEntity commentIt(@AuthenticationPrincipal UserLogin authUser,
                                    @PathVariable("publishId") Long publishId,
                                    @RequestParam String text) {
        PublishService.STATUS status = publishService.commentIt(publishId, null, authUser, text);
        switch (status) {
            case success:
                return ResponseEntity.status(201).body(R.code(201).message("评论成功").build());
            case not_found_publish:
                return ResponseEntity.status(404).body(R.code(404).message("找不到该动态，无法评论").build());
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }

    @ApiOperation(value = "评论【登录】", notes = "对某一条动态评论进行回复，必须是已登陆用户")
    @ApiResponses({
            @ApiResponse(code = 201, message = "回复成功"),
            @ApiResponse(code = 400, message = "找不到该评论，无法进行回复"),
            @ApiResponse(code = 404, message = "找不到该动态，无法评论/回复")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{publishId}/comment/{fatherId}")
    public ResponseEntity replyIt(@AuthenticationPrincipal UserLogin authUser,
                                  @PathVariable("publishId") Long publishId,
                                  @PathVariable(value = "fatherId") Long fatherId,
                                  @RequestParam String text) {
        PublishService.STATUS status = publishService.commentIt(publishId, fatherId, authUser, text);
        switch (status) {
            case success:
                return ResponseEntity.status(201).body(R.code(201).message("回复成功").build());
            case not_found_publish:
                return ResponseEntity.status(404).body(R.code(404).message("找不到该动态，无法评论/回复").build());
            case not_existed:
                return ResponseEntity.status(400).body(R.code(404).message("找不到该评论，无法进行回复").build());
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }

    @ApiOperation(value = "删除评论/回复【登录】", notes = "删除某一条动态评论/回复，必须是已登陆用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "评论成功"),
            @ApiResponse(code = 404, message = "删除失败，找不到该评论/回复")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{publishId}/comment/{commentId}")
    public ResponseEntity deleteComment(@AuthenticationPrincipal UserLogin authUser,
                                        @PathVariable(value = "publishId", required = false) Long publishId,
                                        @PathVariable(value = "commentId") Long commentId) {
        PublishService.STATUS status = publishService.deleteComment(commentId);
        switch (status) {
            case success:
                return ResponseEntity.status(200).body(R.code(200).message("删除评论成功").build());
            case not_existed:
                return ResponseEntity.status(404).body(R.code(404).message("删除失败，找不到该评论/回复").build());
            default:
                return ResponseEntity.status(500).body(R.code(500).message("未知异常").build());
        }
    }
}
