package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface AnswerService {

    ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto);
}
