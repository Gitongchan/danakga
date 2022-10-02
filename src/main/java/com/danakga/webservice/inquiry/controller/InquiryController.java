package com.danakga.webservice.inquiry.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.inquiry.dto.request.ReqInquiryDto;
import com.danakga.webservice.inquiry.service.InquiryService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class InquiryController {

    private final InquiryService inquiryService;

    /* 문의 사항 작성 */
    @PostMapping("/inquiry/write")
    public ResResultDto inquiryWrite(@LoginUser UserInfo userInfo,
                                     @RequestBody ReqInquiryDto reqInquiryDto) {

        return inquiryService.inquiryWrite(userInfo, reqInquiryDto);
    }

    /* 문의 사항 수정 */
    @PutMapping("/inquiry/edit/{in_id}")
    public ResResultDto inquiryEdit(@LoginUser UserInfo userInfo,
                                    @RequestBody ReqInquiryDto reqInquiryDto,
                                    @PathVariable Long in_id) {

        return inquiryService.inquiryEdit(userInfo, reqInquiryDto, in_id);
    }
    
    /* 문의 사항 삭제 상태 변경 */
    @PutMapping("/inquiry/delete/{in_id}")
    public ResResultDto inquiryDelete(@LoginUser UserInfo userInfo,
                                      @PathVariable Long in_id) {

        return inquiryService.inquiryDelete(userInfo, in_id);
    }
}
