package com.danakga.webservice.admin.controller;

import com.danakga.webservice.admin.dto.response.ResAdminProductListDto;
import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    /* 관리자 댓글, 대댓글 검색 *
    * category = 전체, 내용, 작성자
    * sort = N, Y
    * type = 댓글, 대댓글
    * content = 검색어
    * 전체 검색은 2번 url 사용
    */
    @GetMapping({"/commentSearch/{category}/{sort}/{type}/{content}", "/commentSearch/{category}/{sort}/{type}"})
    public ResCommentListDto adminCommentSearch(@LoginUser UserInfo userInfo,
                                                @PathVariable("category") String category,
                                                @PathVariable("sort") String sort,
                                                @PathVariable("type") String type,
                                                @PathVariable(value = "content", required = false) String content,
                                                Pageable pageable, int page) {

        if(content == null) {
            content = "";
        }

        return adminActivityService.adminCommentSearch(userInfo, pageable, page, category, sort, type, content);
    }
    
    /* 관리자 댓글, 대댓글 삭제 */
    @DeleteMapping("/commentDelete/{bd_id}/{cm_id}")
    public ResResultDto adminCommentDelete(@LoginUser UserInfo userInfo,
                                           @PathVariable("bd_id") Long bd_id,
                                           @PathVariable("cm_id") Long cm_id) {
        return adminActivityService.adminCommentDelete(userInfo, bd_id, cm_id);
    }

    @DeleteMapping("/commentAnswerDelete/{bd_id}/{cm_id}/{an_id}")
    public ResResultDto adminCommentAnswerDelete(@LoginUser UserInfo userInfo,
                                                 @PathVariable("bd_id") Long bd_id,
                                                 @PathVariable("cm_id") Long cm_id,
                                                 @PathVariable("an_id") Long an_id) {

        return adminActivityService.adminCommentAnswerDelete(userInfo, bd_id, cm_id, an_id);
    }

    
    
    /* ======================================= 상품 ======================================= */

    /* 관리자 상품 목록
    * 상품은 삭제 상태 값 없어서 바로 출력
    * */
    @GetMapping("/productList")
    public ResAdminProductListDto adminProductList(@LoginUser UserInfo userInfo,
                                                   Pageable pageable, int page) {

        return adminActivityService.adminProductList(userInfo, pageable, page);
    }
    
    /* 관리자 상품 검색
    *  category = 가게, 상품, 브랜드
    *  content = 검색어
    * */
    @GetMapping("/productSearch/{category}/{content}")
    public ResAdminProductListDto adminProductSearch(@LoginUser UserInfo userInfo,
                                                      Pageable pageable, int page,
                                                      @PathVariable("category") String category,
                                                      @PathVariable("content") String content) {

        return adminActivityService.adminProductSearch(userInfo, pageable, page, category, content);
    }

    /* 관리자 상품 삭제 */
    @DeleteMapping("/productDelete/{c_id}/{p_id}")
    public ResResultDto adminProductDelete(@LoginUser UserInfo userInfo,
                                           @PathVariable("c_id") Long c_id,
                                           @PathVariable("p_id") Long p_id) {

        return adminActivityService.adminProductDelete(userInfo, c_id, p_id);
    }

    

    /* ======================================= 후기 ======================================= */

    /* 관리자 후기 목록 조회
    *  sort = N, Y
    * */
    @GetMapping("/reviewList/{sort}")
    public ResReviewListDto adminReviewList(@LoginUser UserInfo userInfo,
                                            Pageable pageable, int page,
                                            @PathVariable("sort") String sort) {

        return adminActivityService.adminReviewList(userInfo, pageable, page, sort);
    }

    /* 관리자 후기 검색 */
    @GetMapping("/reviewSearch/{category}/{sort}/{content}")
    public ResReviewListDto adminReviewSearch(@LoginUser UserInfo userInfo,
                                              Pageable pageable, int page,
                                              @PathVariable("category") String category,
                                              @PathVariable("sort") String sort,
                                              @PathVariable("content") String content) {

        return adminActivityService.adminReviewSearch(userInfo, pageable, page, category, sort, content);
    }

    @DeleteMapping("/reviewDelete/{c_id}/{p_id}/{r_id}")
    public ResResultDto adminReviewDelete(@LoginUser UserInfo userInfo,
                                          @PathVariable("c_id") Long c_id,
                                          @PathVariable("p_id") Long p_id,
                                          @PathVariable("r_id") Long r_id) {

        return adminActivityService.adminReviewDelete(userInfo, c_id, p_id, r_id);
    }

}
