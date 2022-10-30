package com.danakga.webservice.qna.controller;

import com.danakga.webservice.qna.site.dto.response.ResQnADto;
import com.danakga.webservice.qna.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/QnA")
public class QnAPermitAllController {

    private final QnAService qnaService;

    /* 사이트 문의사항 목록 */
    @GetMapping("/list")
    public ResQnADto qnaList(Pageable pageable, int page) {

        return qnaService.qnaList(pageable, page);
    }
    
    /* 사이트 문의사항 조회 */
    @GetMapping("/post/{q_id}")
    public ResQnADto qnaPost(@PathVariable("q_id") Long q_id) {

        return qnaService.qnaPost(q_id);
    }
}
