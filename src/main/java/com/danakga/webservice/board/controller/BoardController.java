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
    public ResBoardWriteDto write(@RequestBody ReqBoardWriteDto reqBoardWriteDto) {
        //@Login user 없이는 값 들어감 일단
        //시큐리티 설정에 csrf().disable() 없어서 post 안됐었음
        return boardService.write(reqBoardWriteDto);
    }

//    @GetMapping("/post/edit/{id}")
//    public String edit(@PathVariable Long id, Board board) {
//        return "/post/edit";
//    }

}
