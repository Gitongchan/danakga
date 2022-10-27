package com.danakga.webservice.qna.site.service;

import com.danakga.webservice.qna.site.dto.request.ReqSiteAnswerDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface SiteAnswerService {

    /* 가게 문의사항 답변 작성 */
    ResResultDto siteAnswerWrite(UserInfo userInfo, ReqSiteAnswerDto reqAnswerDto);
}
