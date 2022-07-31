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
public class CommentsController {

    private final CommentService commentService;

    // 댓글 작성 (게시글 id, 댓글 내용 받기)
    @PostMapping("/comment/write/{bd_id}")
    public ResResultDto commentsWrite(@LoginUser UserInfo userInfo,
                                      @Valid @RequestBody ReqCommentDto reqCommentDto,
                                      @PathVariable("bd_id") Long bd_id) {

        Long result = commentService.commentsWrite(userInfo, reqCommentDto, bd_id);
        return result == -1L ?
                new ResResultDto(result, "댓글 작성 실패") : new ResResultDto(result, "댓글 작성 성공");
    }

    // 댓글 수정 (게시글 id, 수정 댓글 id 받기)
    // 댓글의 id값도 받아서 정확히 해당 댓글만 수정
    @PutMapping("/comment/edit/{bd_id}/{cm_id}")
    public ResResultDto commentsEdit(@LoginUser UserInfo userInfo,
                                     @PathVariable("bd_id") Long bd_id,
                                     @PathVariable("cm_id") Long cm_id,
                                     @RequestBody ReqCommentDto reqCommentDto) {

        Long result = commentService.commentsEdit(bd_id, cm_id, userInfo, reqCommentDto);
        return result == -1L ?
                new ResResultDto(result, "댓글 수정 실패") : new ResResultDto(result, "댓글 수정 성공");
    }

    // 댓글 삭제 여부 변경 (게시글 id, 수정 댓글 id 받기)
    // 마찬가지로 댓글 id 받아서 해당 댓글만 삭제
    @PutMapping("/comment/delete/{bd_id}/{cm_id}")
    public ResResultDto commentsDelete(@PathVariable(value = "bd_id") Long bd_id,
                                       @PathVariable(value = "cm_id") Long cm_id,
                                       @LoginUser UserInfo userInfo) {

        //게시글 삭제 여부 변경 로직 실행
        Long result = commentService.commentsDelete(bd_id, cm_id, userInfo);

        return result == -1L ?
                new ResResultDto(result, "댓글 삭제 실패") : new ResResultDto(result, "댓글 삭제 성공");
    }

    /*

     * 참고 중인 블로그
     * https://velog.io/@yoho98/%EA%B2%8C%EC%8B%9C%EA%B8%80-%EB%8C%93%EA%B8%80-%EB%8C%80%EB%8C%93%EA%B8%80%EB%AC%B4%ED%95%9C%EB%8C%93%EA%B8%80-%EB%A1%9C%EC%A7%81
     * https://www.youtube.com/watch?v=bhnDSyiPvaY&t=186s

     * 07/27 대댓글 작성 완료, 댓글 작성 로직 변경
     * 07/29 대댓글 작성 최종 완료 (depth 값 변경)
     * 07/29 대댓글 수정 테스트 완료

     */

    //대댓글 작성
    @PostMapping("/comment/answer/write/{bd_id}/{cm_id}")
    public ResResultDto answerWrite(@PathVariable(value = "bd_id") Long bd_id,
                                    @PathVariable(value = "cm_id") Long cm_id,
                                    @RequestBody ReqCommentDto reqCommentDto,
                                    @LoginUser UserInfo userInfo) {

        Long result = commentService.answerWrite(userInfo, reqCommentDto, bd_id, cm_id);

        return result == -1L ?
                new ResResultDto(result, "대댓글 작성 실패") : new ResResultDto(result, "대댓글 작성 성공");
    }

    //대댓글 수정
    @PutMapping("/comment/answer/edit/{bd_id}/{cm_id}/{an_id}")
    public ResResultDto answerEdit(@PathVariable(value = "bd_id") Long bd_id, //게시글 id
                                   @PathVariable(value = "cm_id") Long cm_id, //댓글 id
                                   @PathVariable(value = "an_id") Long an_id, //대댓글의 id
                                   @RequestBody ReqCommentDto reqCommentDto,
                                   @LoginUser UserInfo userInfo) {

        Long result = commentService.answerEdit(userInfo, reqCommentDto, bd_id, cm_id, an_id);

        return result == -1L ?
                new ResResultDto(result, "대댓글 수정 실패") : new ResResultDto(result, "대댓글 수정 성공");
    }
    
    
    //대댓글 삭제 여부 변경
    @PutMapping("/comment/answer/delete/{bd_id}/{cm_id}/{an_id}")
    public ResResultDto answerDelete(@PathVariable(value = "bd_id") Long bd_id, //게시글 id
                                     @PathVariable(value = "cm_id") Long cm_id, //댓글 id
                                     @PathVariable(value = "an_id") Long an_id, //대댓글 id
                                     @LoginUser UserInfo userInfo) {
        
        Long result = commentService.answerDelete(userInfo, bd_id, cm_id, an_id);

        return result == -1L ?
                new ResResultDto(result, "대댓글 삭제 실패") : new ResResultDto(result, "대댓글 삭제 성공");
    }

}
