package com.danakga.webservice.siteinquiry.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.siteinquiry.dto.request.ReqSiteInquiryDto;
import com.danakga.webservice.siteinquiry.service.SiteInquiryService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SiteInquiryController {

    private final SiteInquiryService inquiryService;

    /* 문의 사항 작성 */
    @PostMapping("/siteInquiry/write")
    public ResResultDto siteInquiryWrite(@LoginUser UserInfo userInfo,
                                         @RequestBody ReqSiteInquiryDto reqInquiryDto) {

        return inquiryService.siteInquiryWrite(userInfo, reqInquiryDto);
    }

    /* 문의사항 수정 */
    @PutMapping("/siteInquiry/edit/{sin_id}")
    public ResResultDto siteInquiryEdit(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqSiteInquiryDto reqInquiryDto,
                                        @PathVariable Long sin_id) {

        return inquiryService.siteInquiryEdit(userInfo, reqInquiryDto, sin_id);
    }
    
    /* 문의사항 삭제 상태 변경 */
    @PutMapping("/siteInquiry/delete/{sin_id}")
    public ResResultDto siteInquiryDelete(@LoginUser UserInfo userInfo,
                                          @PathVariable Long sin_id) {

        return inquiryService.siteInquiryDelete(userInfo, sin_id);
    }
}
