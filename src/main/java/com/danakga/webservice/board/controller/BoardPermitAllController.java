package com.danakga.webservice.board.controller;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardPermitAllController {

    private final BoardService boardService;
    private final CommentService commentService;

    //게시판 목록
    @GetMapping("/list/{type}")
    public ResBoardListDto boardList(Pageable pageable,
                                     @PathVariable("type") String boardType,
                                     int page) {

        return boardService.boardList(pageable, boardType, page);
    }

    //게시글 조회
    @GetMapping("/post/{id}")
    public ResBoardPostDto getPost(@PathVariable("id") Long id,
                                   HttpServletRequest request, HttpServletResponse response) {

        return boardService.getPost(id, request, response);
    }

    //댓글 조회
    @GetMapping("/post/comments/{id}")
    public ResCommentListDto commentsList(@PathVariable("id") Long id,
                                         Pageable pageable, int page) {

        return commentService.commentsList(id, pageable, page);
    }

    //개별 댓글 조회
    @GetMapping("/comment/check/{cm_id}")
    public ResCommentListDto writeComments(@PathVariable(value = "cm_id") Long cm_id) {

        return commentService.writeComments(cm_id);
    }
    
    //게시판 검색
    //제목 카테고리에 값이 없으면 defaultvalue로 ""지정 후 서비스 로직 실행
    @GetMapping("/list/search/{type}")
    public ResBoardListDto boardSearch(Pageable pageable, int page,
                                       @RequestParam(value = "category") String category,
                                       @RequestParam(value = "content", defaultValue = "") String content,
                                       @PathVariable(value = "type") String boardType) {

        return boardService.boardSearch(pageable, category, content, boardType, page);
    }
}
