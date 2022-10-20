package com.danakga.webservice.qna.service;

import com.danakga.webservice.qna.dto.request.ReqQnADto;
import com.danakga.webservice.qna.dto.response.ResQnADto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface QnAService {
    
    /* 문의사항 목록 */
    ResQnADto qnaList(Pageable pageable, int page);

    /* 문의사항 조회 */
    ResQnADto qnaPost(Long q_id);

    /* 문의사항 작성 */
    ResResultDto qnaWrite(UserInfo userInfo, ReqQnADto reqQnADto);

    /* 문의사항 수정 */
    ResResultDto qnaEdit(UserInfo userInfo, ReqQnADto reqQnADto, Long q_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto qnaDelete(UserInfo userInfo, Long q_id);

}
