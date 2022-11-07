package com.danakga.webservice.qna.controller.productanswer;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.service.AnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ProductAnswerController {

    private final AnswerService AnswerService;

    /* 가게 문의사항 답변 작성 */
    @PostMapping("/product_answer/write/{p_id}/{qn_id}")
    public ResResultDto productAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqAnswerDto reqAnswerDto,
                                        @PathVariable("p_id") Long p_id,
                                        @PathVariable("qn_id") Long qn_id) {

        return AnswerService.productAnswerWrite(userInfo, reqAnswerDto, p_id, qn_id);
    }
    
    /* 가게 문의사항 답변 수정 */
    @PutMapping("/product_answer/edit/{p_id}/{qn_id}/{an_id}")
    public ResResultDto productAnswerEdit(@LoginUser UserInfo userInfo,
                                       @RequestBody ReqAnswerDto reqAnswerDto,
                                       @PathVariable("p_id") Long p_id,
                                       @PathVariable("qn_id") Long qn_id,
                                       @PathVariable("an_id") Long an_id) {

        return AnswerService.productAnswerEdit(userInfo, reqAnswerDto, p_id, qn_id, an_id);
    }

    /* 가게 문의사항 답변 삭제 */
    @PutMapping("/product_answer/delete/{p_id}/{qn_id}/{an_id}")
    public ResResultDto productAnswerDelete(@LoginUser UserInfo userInfo,
                                         @PathVariable("p_id") Long p_id,
                                         @PathVariable("qn_id") Long qn_id,
                                         @PathVariable("an_id") Long an_id) {
        
        return AnswerService.productAnswerDelete(userInfo, p_id, qn_id, an_id);
    }
}
