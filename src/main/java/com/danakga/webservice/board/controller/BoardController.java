package com.danakga.webservice.board.controller;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.FilesService;
import com.danakga.webservice.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    //게시글 작성
    //파일은 RequestBody로 받게되면 Excption 발생 , RequestParam, Part 둘 중 하나로 받기
    // RequestBody는 json 형태의 데이터, file은 Multipart/form-data
    @PostMapping("/postwrite")
    public ResBoardWriteDto write(@Valid @RequestBody ReqBoardWriteDto reqBoardWriteDto, @LoginUser UserInfo userInfo,
                                  @RequestParam(value= "images", required = false) List<MultipartFile> files,
                                  ReqFileUploadDto reqFileUploadDto) {

        return boardService.write(reqBoardWriteDto, userInfo, reqFileUploadDto, files);
    }

    //게시글 수정
//    @PutMapping("/post/edit/{id}")
//    public String edit(@PathVariable Long id, @LoginUser UserInfo userInfo,Board board) {
//        return boardService.update(userInfo, board);
//    }

}
