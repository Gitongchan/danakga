package com.danakga.webservice.board.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired private final BoardService boardService;

    //게시판 목록, 페이징
    @GetMapping("/list")
    public List<ResBoardListDto> list(@PageableDefault(page = 0, size = 10,direction = Sort.Direction.DESC) Pageable pageable) {
        return boardService.boardList(pageable);
    }

    //게시글 조회
    @GetMapping("/post/{id}")
    public ResBoardPostDto getpost(@PathVariable("id") Long id) {
        return  boardService.getpost(id);
    }

    //게시글 작성
    @PostMapping(value = "/postwrite", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto write(@LoginUser UserInfo userInfo,
                              @Valid @RequestPart ReqBoardWriteDto reqBoardWriteDto,
                              @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        //게시글 작성 로직 실행
        Long result = boardService.write(reqBoardWriteDto, userInfo, files);

        if(result.equals(-2L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다(이미지 업로드 오류)");
        } else if(result.equals(-1L)) {
            return new ResResultDto(result, "게시글 등록 실패 했습니다");
        } else {
            return new ResResultDto(result, "게시글을 작성 했습니다.");
        }
    }

    //게시글 수정
//    @PutMapping("/post/edit/{id}")
//    public ResBoardUpdateDto edit(@PathVariable Long bd_id, @LoginUser UserInfo userInfo, Board board) {
//        return boardService.edit(userInfo, board);
//    }

}
