package com.danakga.webservice.board.controller;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private final BoardService boardService;

    //게시판 목록
    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    //게시글 작성
    @PostMapping("/postwrite")
    public ResBoardWriteDto write(@Valid @RequestBody ReqBoardWriteDto reqBoardWriteDto, @LoginUser UserInfo userInfo) {
        return boardService.write(reqBoardWriteDto, userInfo);
    }

    //게시글 수정
//    @PutMapping("/post/edit/{id}")
//    public String edit(@PathVariable Long id, Board board) {
//        return "/post/edit";
//    }

}
