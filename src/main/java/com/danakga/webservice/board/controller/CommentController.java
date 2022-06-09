package com.danakga.webservice.board.controller;

import com.danakga.webservice.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class CommentController {

    private final BoardService boardService;


}
