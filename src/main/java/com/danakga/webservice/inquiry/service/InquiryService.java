package com.danakga.webservice.inquiry.service;

import com.danakga.webservice.inquiry.dto.request.ReqInquiryDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface InquiryService {

    ResResultDto inquiryWrite(UserInfo userInfo, ReqInquiryDto reqInquiryDto);

}
