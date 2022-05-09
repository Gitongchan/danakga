package com.danakga.webservice.board.controller;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    //게시판 목록
    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    //파일 업로드 후 할거
    @GetMapping("/post/{id}")
    public ResPostDto findById(@PathVariable("id") Long bd_id) {
        boardService.bd_IdCheck(bd_id);
        return null;
    }

    //게시글 작성
    //consumes은 들어오는 데이터 타입을 정의할 때 사용하고 mediatype으로 json과 formdata를 받는다
    //@RequestBody는 데이터 형식을 json형태로 받기 때문에 파일을 받아오지 못함
    //HTTP 요청시 multipart/ 로 시작하는 경우는 multipart 요청으로 판단해서 RequestPart로 받아야함
    @PostMapping(value = "/postwrite", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResBoardWriteDto write(@LoginUser UserInfo userInfo,
                                  @Valid @RequestPart ReqBoardWriteDto reqBoardWriteDto,
                                  @RequestPart(value = "images", required = false) List<MultipartFile> files,
                                  ReqFileUploadDto reqFileUploadDto) {

        return boardService.write(reqBoardWriteDto, userInfo, reqFileUploadDto, files);
    }

    //게시글 수정
//    @PutMapping("/post/edit/{id}")
//    public ResBoardUpdateDto edit(@PathVariable Long bd_id, @LoginUser UserInfo userInfo, Board board) {
//        return boardService.edit(userInfo, board);
//    }

}
