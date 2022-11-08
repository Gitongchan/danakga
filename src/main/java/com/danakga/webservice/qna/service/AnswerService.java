package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.dto.response.ResAnswerDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface AnswerService {

    /* 문의사항 답변 조회 */
    ResAnswerDto answerPost(Long qn_id, Pageable pageable, int page);
    
    /* 가게 문의사항 답변 (manager) */

    /* 가게 문의사항 답변 작성 */
    ResResultDto productAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long p_id, Long qn_id);
    
    /* 가게 문의사항 답변 수정 */
    ResResultDto productAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long p_id, Long qn_id, Long a_id);
    
    /* 가게 문의사항 답변 삭제 */
    ResResultDto productAnswerDelete(UserInfo userInfo, Long p_id, Long qn_id, Long a_id);


    /* 사이트 문의사항 답변 (admin) */

    /* 사이트 문의사항 답변 작성 */
    ResResultDto siteAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long qn_id);

    /* 사이트 문의사항 답변 수정 */
    ResResultDto siteAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long qn_id, Long an_id);

    /* 사이트 문의사항 답변 삭제 */
    ResResultDto siteAnswerDelete(UserInfo userInfo, Long qn_id, Long an_id);
}
