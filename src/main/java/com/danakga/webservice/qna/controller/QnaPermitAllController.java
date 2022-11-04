package com.danakga.webservice.qna.controller;

import com.danakga.webservice.qna.dto.response.ResQnaDto;
import com.danakga.webservice.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaPermitAllController {

    private final QnaService qnaService;

    /* 문의사항 목록 */
    /* /list/{q_sort}/{c_id} 가게 문의사항 목록 --- /list/{q_sort} 사이트, 전체 가게 문의사항 목록*/
    /* 0 = 사이트, 1 = 가게 */
    @GetMapping({"/list/company/{c_id}", "/list/site/{q_sort}"})
    public ResQnaDto qnaList(Pageable pageable, int page,
                             @PathVariable(value = "q_sort", required = false) Integer q_sort,
                             @PathVariable(value = "c_id", required = false) Long c_id) {

        return qnaService.qnaList(pageable, q_sort, page, c_id);
    }
    
    /* 문의사항 조회 */
    @GetMapping("/post/{qn_id}")
    public ResQnaDto qnaPost(@PathVariable("qn_id") Long qn_id) {

        return qnaService.qnaPost(qn_id);
    }
}
