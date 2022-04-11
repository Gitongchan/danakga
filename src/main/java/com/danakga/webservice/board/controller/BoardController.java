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
        //가져다 쓰는건 이렇게 get으로 꺼내와서 쓰는게 맞는듯
        return boardService.write(reqBoardWriteDto, userInfo.getUserid());
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable Long id, Board board) {
        return "/post/edit";
    }

}
