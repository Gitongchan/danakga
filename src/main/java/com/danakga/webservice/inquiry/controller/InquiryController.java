package com.danakga.webservice.inquiry.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.inquiry.dto.request.ReqInquiryDto;
import com.danakga.webservice.inquiry.service.InquiryService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class InquiryController {

    private final InquiryService inquiryService;

    /* 문의 작성 */
    @PostMapping("/inquiry/write")
    public ResResultDto inquireWrite(@LoginUser UserInfo userInfo,
                                     @RequestBody ReqInquiryDto reqInquiryDto) {

        return inquiryService.inquiryWrite(userInfo, reqInquiryDto);
    }
}
