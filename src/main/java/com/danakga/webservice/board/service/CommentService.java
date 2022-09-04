package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    //댓글 조회
    ResCommentListDto commentsList (Long cm_id, Pageable pageable, int page);

    //댓글 작성
    ResResultDto commentsWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id);
    
    //댓글 수정
    ResResultDto commentsEdit(Long bd_id, Long cm_id, UserInfo userInfo, ReqCommentDto reqCommentDto);

    //댓글 삭제 여부 변경
    ResResultDto commentsDelete(Long bd_id, Long cm_id, UserInfo userInfo);

    //개별 댓글 조회
    ResCommentListDto writeComments(Long cm_id);



                                /* ================= 대댓글 ================= */

    //대댓글 작성
    ResResultDto answerWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id);

    //대댓글 수정
    ResResultDto answerEdit(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id, Long an_id);
    
    //대댓글 삭제 여부 변경
    ResResultDto answerDelete(UserInfo userInfo, Long bd_id, Long cm_id, Long an_id);

}
