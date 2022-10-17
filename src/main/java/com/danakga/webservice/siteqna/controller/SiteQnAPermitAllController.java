package com.danakga.webservice.siteqna.controller;

import com.danakga.webservice.siteqna.dto.response.ResSiteQnADto;
import com.danakga.webservice.siteqna.service.SiteQnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/siteQnA")
public class SiteQnAPermitAllController {

    private final SiteQnAService siteQnAService;

    /* 문의사항 목록 */
    @GetMapping("/list")
    public ResSiteQnADto siteQnAList(Pageable pageable, int page) {

        return siteQnAService.siteQnAList(pageable, page);
    }
    
    /* 문의사항 조회 */
    @GetMapping("/post/{sq_id}")
    public ResSiteQnADto siteQnAPost(@PathVariable("sq_id") Long sq_id) {

        return siteQnAService.siteQnAPost(sq_id);
    }
}
