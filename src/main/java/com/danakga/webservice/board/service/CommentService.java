package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    //댓글 조회
    ResCommentListDto commentsList (Long cm_id, Pageable pageable, int page);

    //댓글 작성
    Long commentsWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id);
    
    //댓글 수정
    Long commentsEdit(Long bd_id, Long cm_id, UserInfo userInfo, ReqCommentDto reqCommentDto);

    //댓글 삭제 여부 변경
    Long commentsDelete(Long bd_id, Long cm_id, UserInfo userInfo);
    
    //개별 댓글 조회
    ResCommentListDto writeComments(Long cm_id);
    
    //대댓글 작성
    Long answerWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id);
}
