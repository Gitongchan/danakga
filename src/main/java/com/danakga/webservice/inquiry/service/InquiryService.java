package com.danakga.webservice.inquiry.service;

import com.danakga.webservice.inquiry.dto.request.ReqInquiryDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface InquiryService {

    /* 문의 사항 작성 */
    ResResultDto inquiryWrite(UserInfo userInfo, ReqInquiryDto reqInquiryDto);

    /* 문의 사항 수정 */
    ResResultDto inquiryEdit(UserInfo userInfo, ReqInquiryDto reqInquiryDto, Long in_id);

    /* 문의 사항 삭제 상태 변경 */
    ResResultDto inquiryDelete(UserInfo userInfo, Long in_id);

}
