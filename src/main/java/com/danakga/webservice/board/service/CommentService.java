package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Long commentWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long id);

    ResCommentListDto commentList (Long id, Pageable pageable, int page);
}
