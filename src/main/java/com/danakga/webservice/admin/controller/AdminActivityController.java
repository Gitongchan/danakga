package com.danakga.webservice.admin.controller;

import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value="/admin")
@RestController
public class AdminActivityController {

    private final AdminActivityService adminActivityService;

    /* 관리자 게시판 목록 */
    /* sort == deleted N, Y */
    @GetMapping("/boardList/{sort}")
    public ResBoardListDto adminBoardList(@LoginUser UserInfo userInfo,
                                          Pageable pageable, int page,
                                          @PathVariable("sort") String sort) {

        return adminActivityService.adminBoardListDto(userInfo, sort, pageable, page);
    }
    
    /* 관리자 게시글 조회 */
    @GetMapping("/boardPost/{bd_id}")
    public ResBoardPostDto adminBoardPost(@LoginUser UserInfo userInfo,
                                          @PathVariable("bd_id") Long bd_id) {

        return adminActivityService.adminBoardPost(userInfo, bd_id);
    }
    
    /* 관리자 게시판 검색 */
    @GetMapping("/boardSearch/{category}/{sort}/{content}")
    public ResBoardListDto adminBoardSearch(@LoginUser UserInfo userInfo,
                                            Pageable pageable, int page,
                                            @PathVariable("category") String category,
                                            @PathVariable("sort") String sort,
                                            @PathVariable("content") String content) {

        return adminActivityService.adminBoardSearch(userInfo, pageable, page, category, sort, content);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/postDelete/{bd_id}")
    public ResResultDto adminPostDeleteDto(@LoginUser UserInfo userInfo,
                                            @PathVariable("bd_id") Long bd_id) {

        return adminActivityService.adminPostDelete(userInfo, bd_id);
    }
}
