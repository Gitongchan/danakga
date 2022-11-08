package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqQnaDto;
import com.danakga.webservice.qna.dto.response.ResAnswerDto;
import com.danakga.webservice.qna.dto.response.ResQnaDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface QnaService {
    
    /* 문의사항 목록 */
    ResQnaDto qnaList(Pageable pageable, Integer q_sort, int page, Long p_id);

    /* 가게 문의사항 전체 목록 */
    ResQnaDto companyQnaList(Pageable pageable, int page, Long c_id);

    /* 문의사항 조회 */
    ResQnaDto qnaPost(Long qn_id);

    /* 문의사항 작성 */
    ResResultDto qnaWrite(UserInfo userInfo, ReqQnaDto reqQnaDto, Long c_id);

    /* 문의사항 수정 */
    ResResultDto qnaEdit(UserInfo userInfo, ReqQnaDto reqQnaDto, Long p_id, Long qn_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto qnaDelete(UserInfo userInfo, Long p_id, Long qn_id);

}
