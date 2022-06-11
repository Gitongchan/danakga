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

    //댓글 작성
    @PostMapping("/comment/write/{id}")
    public ResResultDto commentWrite(@LoginUser UserInfo userInfo,
                                     @Valid @RequestBody ReqCommentDto reqCommentDto,
                                     @PathVariable("id") Long id) {

        Long result = commentService.commentWrite(userInfo, reqCommentDto, id);
        return result == -1L ?
                new ResResultDto(result, "댓글 작성 실패") : new ResResultDto(result, "댓글 작성 성공");
    }
}
