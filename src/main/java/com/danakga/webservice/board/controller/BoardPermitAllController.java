package com.danakga.webservice.board.controller;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //개별 게시글 조회
    @GetMapping("/post/{bd_id}")
    public ResBoardPostDto getPost(@PathVariable("bd_id") Long bd_id,
                                   HttpServletRequest request, HttpServletResponse response) {

        return boardService.getPost(bd_id, request, response);
    }

    //댓글 조회
    @GetMapping("/post/comments/{cm_id}")
    public ResCommentListDto commentsList(@PathVariable("cm_id") Long cm_id,
                                         Pageable pageable, int page) {

        return commentService.commentsList(cm_id, pageable, page);
    }

    //개별 댓글 조회
    @GetMapping("/comment/check/{cm_id}")
    public ResCommentListDto writeComments(@PathVariable(value = "cm_id") Long cm_id) {

        return commentService.writeComments(cm_id);
    }

    //게시판 검색
    //제목 카테고리에 값이 없으면 defaultvalue로 ""지정 후 서비스 로직 실행,
    //content값이 들어오지 않았을 때 url을 하나 더 설정하고 content값이 null값으로 처리되어 넘어가지 않게 ""값 담아서 보냄
    @GetMapping({"/list/search/{type}/{category}/{content}", "/list/search/{type}/{category}"})
    public ResBoardListDto boardSearch(Pageable pageable, int page,
                                       @PathVariable(value = "category") String category,
                                       @PathVariable(value = "content", required = false) String content,
                                       @PathVariable(value = "type") String boardType) {

        //content에 null값 들어가서 오류 발생 시키지 않게 2번째 url 사용 시 ""값 담아서 서비스로 전달
        if(content == null) {
            content = "";
        }

        return boardService.boardSearch(pageable, category, content, boardType, page);
    }
}
