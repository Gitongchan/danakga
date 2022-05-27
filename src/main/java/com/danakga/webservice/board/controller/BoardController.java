package com.danakga.webservice.board.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired private final BoardService boardService;

    //게시판 목록,구분,페이징
    @GetMapping("/list/{type}")
    public List<ResBoardListDto> list(Pageable pageable, @PathVariable("type") String board_type, int page) {
        return boardService.boardList(pageable, board_type, page);
    }

    //게시글 조회
    @GetMapping("/post/{id}")
    public ResBoardPostDto getpost(@PathVariable("id") Long id,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                   ) {
        return boardService.getpost(id, request, response);
    }

    //게시글 작성
    @PostMapping(value = "/postwrite", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto write(@LoginUser UserInfo userInfo,
                              @Valid @RequestPart(value="keys") ReqBoardDto reqBoardDto,
                              @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        //게시글 작성 로직 실행
        Long result = boardService.write(reqBoardDto, userInfo, files);

        if(result.equals(-2L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다(이미지 업로드 오류)");
        } else if(result.equals(-1L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다");
        } else {
            return new ResResultDto(result, "게시글을 작성 했습니다.");
        }
    }

    //게시글 수정
    @PutMapping("/post/edit/{id}")
    public Long edit(@PathVariable("id") Long id, @LoginUser UserInfo userInfo) {
        return boardService.edit(id, userInfo);
    }

    @DeleteMapping("post/delete/{id}")
    public Long delete(@PathVariable("id") Long id, @LoginUser UserInfo userInfo) {
        return boardService.delete(id, userInfo);
    }

}
