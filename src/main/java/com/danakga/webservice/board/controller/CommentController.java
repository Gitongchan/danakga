package com.danakga.webservice.board.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.service.CommentService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment/write/{bd_id}")
    public ResResultDto commentsWrite(@LoginUser UserInfo userInfo,
                                      @Valid @RequestBody ReqCommentDto reqCommentDto,
                                      @PathVariable("bd_id") Long bd_id) {

        return commentService.commentsWrite(userInfo, reqCommentDto, bd_id);
    }

    // 댓글 수정
    @PutMapping("/comment/edit/{bd_id}/{cm_id}")
    public ResResultDto commentsEdit(@LoginUser UserInfo userInfo,
                                     @PathVariable("bd_id") Long bd_id,
                                     @PathVariable("cm_id") Long cm_id,
                                     @RequestBody ReqCommentDto reqCommentDto) {

        return commentService.commentsEdit(bd_id, cm_id, userInfo, reqCommentDto);
    }

    // 댓글 삭제 여부 변경
    @PutMapping("/comment/delete/{bd_id}/{cm_id}")
    public ResResultDto commentsDelete(@PathVariable(value = "bd_id") Long bd_id,
                                       @PathVariable(value = "cm_id") Long cm_id,
                                       @LoginUser UserInfo userInfo) {

        return commentService.commentsDelete(bd_id, cm_id, userInfo);
    }


    /*      ============================ 대댓글 ============================      */

    // 대댓글 작성
    @PostMapping("/comment/answer/write/{bd_id}/{cm_id}")
    public ResResultDto answerWrite(@PathVariable(value = "bd_id") Long bd_id,
                                    @PathVariable(value = "cm_id") Long cm_id,
                                    @RequestBody ReqCommentDto reqCommentDto,
                                    @LoginUser UserInfo userInfo) {

        Long result = commentService.answerWrite(userInfo, reqCommentDto, bd_id, cm_id);

        return new ResResultDto(result, "대댓글 작성 성공");
    }

    // 대댓글 수정
    @PutMapping("/comment/answer/edit/{bd_id}/{cm_id}/{an_id}")
    public ResResultDto answerEdit(@PathVariable(value = "bd_id") Long bd_id, //게시글 id
                                   @PathVariable(value = "cm_id") Long cm_id, //댓글 id
                                   @PathVariable(value = "an_id") Long an_id, //대댓글의 id
                                   @RequestBody ReqCommentDto reqCommentDto,
                                   @LoginUser UserInfo userInfo) {

        Long result = commentService.answerEdit(userInfo, reqCommentDto, bd_id, cm_id, an_id);

        return new ResResultDto(result, "대댓글 수정 성공");
    }
    
    
    /* 대댓글 삭제 여부 변경 */
    @PutMapping("/comment/answer/delete/{bd_id}/{cm_id}/{an_id}")
    public ResResultDto answerDelete(@PathVariable(value = "bd_id") Long bd_id, //게시글 id
                                     @PathVariable(value = "cm_id") Long cm_id, //댓글 id
                                     @PathVariable(value = "an_id") Long an_id, //대댓글 id
                                     @LoginUser UserInfo userInfo) {
        
        Long result = commentService.answerDelete(userInfo, bd_id, cm_id, an_id);

        return new ResResultDto(result, "대댓글 삭제 성공");
    }

}
