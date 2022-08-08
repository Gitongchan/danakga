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

        //게시글 작성 로직 실행
        Long result = boardService.postWrite(reqBoardDto, userInfo, files);

        if(result.equals(-2L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다(이미지 업로드 오류)");
        } else if(result.equals(-1L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다");
        } else {
            return new ResResultDto(result, "게시글을 작성 했습니다.");
        }
    }

    //게시글 수정
    @PutMapping(value = "/post/edit/{bd_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postEdit(@PathVariable(value = "bd_id") Long bd_id,
                                 @LoginUser UserInfo userInfo,
                                 @RequestPart(value = "keys") ReqBoardDto reqBoardDto,
                                 @RequestPart(value = "deletedFiles", required = false) ReqDeletedFileDto reqDeletedFileDto,
                                 @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        //게시글 수정 로직 실행
        Long result = boardService.postEdit(bd_id, userInfo, reqBoardDto, reqDeletedFileDto, files);
        
        if(result.equals(-2L)) {
            return new ResResultDto(result, "게시글 수정에 실패 했습니다(이미지 업로드 오류)");
        } else if(result.equals(-1L)) {
            return new ResResultDto(result, "게시글 수정에 실패 했습니다");
        } else {
            return new ResResultDto(result, "게시글을 수정 했습니다.");
        }
    }

    //게시글 삭제
    @PutMapping("/post/delete/{bd_id}")
    public ResResultDto postDelete(@PathVariable(value = "bd_id") Long bd_id,
                                   @LoginUser UserInfo userInfo) {

        //게시글 삭제 로직 실행
        Long result = boardService.postDelete(bd_id, userInfo);

        return new ResResultDto(result, "게시글이 삭제되었습니다.");
    }

}
