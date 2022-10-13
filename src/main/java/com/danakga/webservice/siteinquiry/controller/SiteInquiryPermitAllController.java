package com.danakga.webservice.siteinquiry.controller;

import com.danakga.webservice.siteinquiry.dto.response.ResSiteInquiryDto;
import com.danakga.webservice.siteinquiry.service.SiteInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/siteInquiry")
public class SiteInquiryPermitAllController {

    private final SiteInquiryService siteInquiryService;

    /* 문의사항 목록 */
    @GetMapping("/list")
    public ResSiteInquiryDto siteInquiryList(Pageable pageable, int page) {

        return siteInquiryService.siteInquiryList(pageable, page);
    }
    
    /* 문의사항 조회 */
    @GetMapping("/post/{sin_id}")
    public ResSiteInquiryDto siteInquiryPost(@PathVariable("sin_id") Long sin_id) {

        return siteInquiryService.siteInquiryPost(sin_id);
    }
}
