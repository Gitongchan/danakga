package com.danakga.webservice.inquiry.controller;

import com.danakga.webservice.inquiry.service.InquiryService;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryPermitAllController {

    private final InquiryService inquiryService;

    /* 문의 사항 목록 */
    @GetMapping("/inquiryList")
    public ResReviewListDto inquiryList(Pageable pageable,
                                        int page) {

        return null;
    }
}
