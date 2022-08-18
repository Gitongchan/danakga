package com.danakga.webservice.board.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.request.ReqDeletedFileDto;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class BoardController {

    private final BoardService boardService;

    //게시글 작성
    @PostMapping(value = "/post/write", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postWrite(@LoginUser UserInfo userInfo,
                                  @Valid @RequestPart(value="keys") ReqBoardDto reqBoardDto,
                                  @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        return boardService.postWrite(reqBoardDto, userInfo, files);
    }

    //게시글 수정
    @PutMapping(value = "/post/edit/{bd_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postEdit(@PathVariable(value = "bd_id") Long bd_id,
                                 @LoginUser UserInfo userInfo,
                                 @RequestPart(value = "keys") ReqBoardDto reqBoardDto,
                                 @RequestPart(value = "deletedFiles", required = false) ReqDeletedFileDto reqDeletedFileDto,
                                 @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        return boardService.postEdit(bd_id, userInfo, reqBoardDto, reqDeletedFileDto, files);
    }

    //게시글 삭제
    @PutMapping("/post/delete/{bd_id}")
    public ResResultDto postDelete(@PathVariable(value = "bd_id") Long bd_id,
                                   @LoginUser UserInfo userInfo) {

        return boardService.postDelete(bd_id, userInfo);
    }

}
