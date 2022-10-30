package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface ShopAnswerService {

    /* 가게 문의사항 답변 작성 */
    ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto);
}
