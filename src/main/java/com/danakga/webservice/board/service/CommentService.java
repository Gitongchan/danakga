package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    //댓글 조회
    ResCommentListDto commentList (Long cm_id, Pageable pageable, int page);

    //댓글 작성
    Long commentWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long id);
    
    //댓글 수정
    Long commentEdit(Long bd_id, Long cm_id, UserInfo userInfo, ReqCommentDto reqCommentDto);

    //댓글 삭제 여부 변경
    Long commentDelete(Long bd_id, Long cm_id, UserInfo userInfo);
    
    //개별 댓글 조회
    ResCommentListDto writeComment(Long cm_id);
}
