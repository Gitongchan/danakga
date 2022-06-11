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

    // 댓글 작성 (게시글 id, 댓글 내용 받기)
    @PostMapping("/comment/write/{id}")
    public ResResultDto commentWrite(@LoginUser UserInfo userInfo,
                                     @Valid @RequestBody ReqCommentDto reqCommentDto,
                                     @PathVariable("id") Long id) {

        Long result = commentService.commentWrite(userInfo, reqCommentDto, id);
        return result == -1L ?
                new ResResultDto(result, "댓글 작성 실패") : new ResResultDto(result, "댓글 작성 성공");
    }

    // 댓글 수정, (게시글 id, 수정 댓글 id 받기)
    // 댓글 아이디 받아오는 이유는 bd_id 로만 하면 동일한 bd_id로 작성된 댓글이 다 수정될 것 같음
    @PutMapping("/comment/edit/{bd_id}/{cm_id}")
    public ResResultDto commentEdit(@LoginUser UserInfo userInfo,
                                    @PathVariable("bd_id") Long bd_id,
                                    @PathVariable("cm_id") Long cm_id,
                                    @RequestBody ReqCommentDto reqCommentDto) {

        Long result = commentService.commentEdit(bd_id, cm_id, userInfo, reqCommentDto);
        return result == -1L ?
                new ResResultDto(result, "댓글 수정 실패") : new ResResultDto(result, "댓글 수정 성공");
    }


}
