package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.user.model.UserInfo;

public interface CommentService {

    Long commentWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long id);
}
