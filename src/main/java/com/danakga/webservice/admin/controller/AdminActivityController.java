package com.danakga.webservice.admin.controller;

import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
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

    /* ======================================= 게시판 ======================================= */

    /* 관리자 게시판 목록 */
    /* sort == deleted N, Y */
    @GetMapping("/boardList/{sort}/{type}")
    public ResBoardListDto adminBoardList(@LoginUser UserInfo userInfo,
                                          Pageable pageable, int page,
                                          @PathVariable("sort") String sort,
                                          @PathVariable("type") String type) {

        return adminActivityService.adminBoardListDto(userInfo, sort, type, pageable, page);
    }
    
    /* 관리자 게시글 조회 */
    @GetMapping("/boardPost/{bd_id}")
    public ResBoardPostDto adminBoardPost(@LoginUser UserInfo userInfo,
                                          @PathVariable("bd_id") Long bd_id) {

        return adminActivityService.adminBoardPost(userInfo, bd_id);
    }
    
    /* 관리자 게시판 검색 */
    /* category = 전체, 제목, 내용, 작성자 
    *  sort = N, Y
    *  type = 자유, 정보
    *  content = 검색어
    * */
    @GetMapping({"/boardSearch/{category}/{sort}/{type}/{content}", "/boardSearch/{category}/{sort}/{type}"})
    public ResBoardListDto adminBoardSearch(@LoginUser UserInfo userInfo,
                                            Pageable pageable, int page,
                                            @PathVariable("category") String category,
                                            @PathVariable("sort") String sort,
                                            @PathVariable("type") String type,
                                            @PathVariable(value = "content", required = false) String content) {

        if(content == null) {
            content = "";
        }

        return adminActivityService.adminBoardSearch(userInfo, pageable, page, category, sort, type, content);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/postDelete/{bd_id}")
    public ResResultDto adminPostDeleteDto(@LoginUser UserInfo userInfo,
                                            @PathVariable("bd_id") Long bd_id) {

        return adminActivityService.adminPostDelete(userInfo, bd_id);
    }


    /* ======================================= 댓글,대댓글 ======================================= */

    /* 관리자 댓글, 대댓글 목록 */
    /* type = 댓글, 대댓글
    *  sort == deleted -> N, Y */
    @GetMapping("/commentList/{type}/{sort}")
    public ResCommentListDto adminCommentList(@LoginUser UserInfo userInfo,
                                              @PathVariable("type") String type,
                                              @PathVariable("sort") String sort,
                                              Pageable pageable, int page) {

        return adminActivityService.adminCommentList(userInfo, type, sort, pageable, page);
    }
}
