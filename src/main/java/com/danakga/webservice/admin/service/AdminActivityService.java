package com.danakga.webservice.admin.service;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface AdminActivityService {

    /* 관리자 게시판 목록 */
    ResBoardListDto adminBoardListDto(UserInfo userInfo, String sort, String type, Pageable pageable, int page);
    
    /* 관리자 게시판 조회 */
    ResBoardPostDto adminBoardPost(UserInfo userInfo, Long bd_id);

    /* 관리자 게시판 검색 */
    ResBoardListDto adminBoardSearch(UserInfo userInfo, Pageable pageable, int page, String category, String sort, String type, String content);

    /* 게시글 삭제 */
    ResResultDto adminPostDelete(UserInfo userInfo, Long bd_id);
    
    /* 관리자 댓글, 대댓글 목록 */
    ResCommentListDto adminCommentList(UserInfo userInfo, String type, String sort, Pageable pageable, int page);
    
    /* 관리자 댓글, 대댓글 검색 */
    ResCommentListDto adminCommentSearch(UserInfo userInfo, Pageable pageable, int page,
                                         String category, String sort, String type, String content);
}
