package com.danakga.webservice.siteinquiry.service;

import com.danakga.webservice.siteinquiry.dto.request.ReqSiteInquiryDto;
import com.danakga.webservice.siteinquiry.dto.response.ResSiteInquiryDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface SiteInquiryService {
    
    /* 문의사항 목록 */
    ResSiteInquiryDto siteInquiryList(Pageable pageable, int page);

    /* 문의사항 조회 */
    ResSiteInquiryDto siteInquiryPost(Long sin_id);

    /* 문의사항 작성 */
    ResResultDto siteInquiryWrite(UserInfo userInfo, ReqSiteInquiryDto reqInquiryDto);

    /* 문의사항 수정 */
    ResResultDto siteInquiryEdit(UserInfo userInfo, ReqSiteInquiryDto reqInquiryDto, Long in_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto siteInquiryDelete(UserInfo userInfo, Long in_id);

}
