package com.danakga.webservice.siteqna.service;

import com.danakga.webservice.siteqna.dto.request.ReqSiteQnADto;
import com.danakga.webservice.siteqna.dto.response.ResSiteQnADto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface SiteQnAService {
    
    /* 문의사항 목록 */
    ResSiteQnADto siteQnAList(Pageable pageable, int page);

    /* 문의사항 조회 */
    ResSiteQnADto siteQnAPost(Long sq_id);

    /* 문의사항 작성 */
    ResResultDto siteQnAWrite(UserInfo userInfo, ReqSiteQnADto reqSieQnADto);

    /* 문의사항 수정 */
    ResResultDto siteQnAEdit(UserInfo userInfo, ReqSiteQnADto reqSieQnADto, Long sq_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto siteQnADelete(UserInfo userInfo, Long sq_id);

}
