package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface ShopAnswerService {
    
    /* 가게 문의사항 답변 (manager) */

    /* 가게 문의사항 답변 작성 */
    ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long c_id, Long qn_id);
    
    /* 가게 문의사항 답변 수정 */
    ResResultDto shopAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long c_id, Long qn_id, Long a_id);
    
    /* 가게 문의사항 답변 삭제 */
    ResResultDto shopAnswerDelete(UserInfo userInfo, Long c_id, Long qn_id, Long a_id);


    /* 사이트 문의사항 답변 (admin) */
}
