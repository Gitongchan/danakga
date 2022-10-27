package com.danakga.webservice.qna.site.service;

import com.danakga.webservice.qna.site.dto.request.ReqSiteQnADto;
import com.danakga.webservice.qna.site.dto.response.ResSiteQnADto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface SiteQnAService {
    
    /* 문의사항 목록 */
    ResSiteQnADto siteQnAList(Pageable pageable, int page);

    /* 문의사항 조회 */
    ResSiteQnADto siteQnAPost(Long siteQ_id);

    /* 문의사항 작성 */
    ResResultDto siteQnAWrite(UserInfo userInfo, ReqSiteQnADto reqQnADto);

    /* 문의사항 수정 */
    ResResultDto siteQnAEdit(UserInfo userInfo, ReqSiteQnADto reqQnADto, Long siteQ_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto siteQnADelete(UserInfo userInfo, Long siteQ_id);

}
