package com.danakga.webservice.qna.controller;

import com.danakga.webservice.qna.dto.response.ResQnADto;
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
    /* 0 = 사이트, 1 = 가게 조회 할 수 있도록  */
    @GetMapping("/list/{q_sort}")
    public ResQnADto qnaList(Pageable pageable, int page,
                             @PathVariable("q_sort") int q_sort) {

        return qnaService.qnaList(pageable, q_sort, page);
    }
    
    /* 사이트 문의사항 조회 */
    @GetMapping("/post/{q_id}")
    public ResQnADto qnaPost(@PathVariable("q_id") Long q_id) {

        return qnaService.qnaPost(q_id);
    }
}
