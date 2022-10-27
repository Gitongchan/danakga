package com.danakga.webservice.qna.site.controller;

import com.danakga.webservice.qna.site.dto.response.ResSiteQnADto;
import com.danakga.webservice.qna.site.service.SiteQnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/QnA")
public class SiteQnAPermitAllController {

    private final SiteQnAService siteQnAService;

    /* 사이트 문의사항 목록 */
    @GetMapping("/list")
    public ResSiteQnADto siteQnAList(Pageable pageable, int page) {

        return siteQnAService.siteQnAList(pageable, page);
    }
    
    /* 사이트 문의사항 조회 */
    @GetMapping("/post/{siteQ_id}")
    public ResSiteQnADto siteQnAPost(@PathVariable("siteQ_id") Long siteQ_id) {

        return siteQnAService.siteQnAPost(siteQ_id);
    }
}
