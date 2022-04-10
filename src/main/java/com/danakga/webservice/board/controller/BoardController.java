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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private final BoardService boardService;

    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    @PostMapping("/write")
    public ResBoardWriteDto write(@LoginUser UserInfo userInfo, @RequestBody ReqBoardWriteDto reqBoardWriteDto) {
        //이렇게 바로 getter로 받아다가 넣어서 쓰는건지 아니면 다른 방법이 있는건지 더 찾아봐야할듯
        return boardService.write(reqBoardWriteDto, userInfo.getUserid());
    }
    
    // 4/4 게시글 수정
//    @GetMapping("/post/edit/{id}")
//    public String edit(@PathVariable Long id, Board board) {
//        return "/post/edit";
//    }

}
