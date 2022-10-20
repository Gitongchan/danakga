package com.danakga.webservice.shopqna.controller;

import com.danakga.webservice.shopqna.dto.response.ResShopQnADto;
import com.danakga.webservice.shopqna.service.ShopQnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shopQnA")
public class ShopQnAPermitAllController {

    private final ShopQnAService shopQnAService;

    /* 문의사항 목록 */
    @GetMapping("/list")
    public ResShopQnADto shopQnAList(Pageable pageable, int page) {

        return shopQnAService.shopQnAList(pageable, page);
    }
    
    /* 문의사항 조회 */
    @GetMapping("/post/{sq_id}")
    public ResShopQnADto shopQnAPost(@PathVariable("sq_id") Long sq_id) {

        return shopQnAService.shopQnAPost(sq_id);
    }
}
