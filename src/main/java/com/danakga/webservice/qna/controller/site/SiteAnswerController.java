package com.danakga.webservice.qna.controller.site;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.service.AnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class SiteAnswerController {

    private final AnswerService answerService;

    /* 사이트 문의사항 답변 작성 */
    @PostMapping("/site_answer/write/{qn_id}")
    public ResResultDto siteAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqAnswerDto reqAnswerDto,
                                        @PathVariable("qn_id") Long qn_id) {

        return answerService.siteAnswerWrite(userInfo, reqAnswerDto, qn_id);
    }
    
    /* 사이트 문의사항 답변 수정 */
    @PutMapping("/site_answer/edit/{qn_id}/{an_id}")
    public ResResultDto siteAnswerEdit(@LoginUser UserInfo userInfo,
                                       @RequestBody ReqAnswerDto reqAnswerDto,
                                       @PathVariable("qn_id") Long qn_id,
                                       @PathVariable("an_id") Long an_id) {
        return answerService.siteAnswerEdit(userInfo, reqAnswerDto, qn_id, an_id);
    }
    
    /* 사이트 문의사항 답변 삭제 */
    @PutMapping("/site_answer/delete/{qn_id}/{an_id}")
    public ResResultDto siteAnswerDelete(@LoginUser UserInfo userInfo,
                                         @PathVariable("qn_id") Long qn_id,
                                         @PathVariable("an_id") Long an_id) {

        return answerService.siteAnswerDelete(userInfo, qn_id, an_id);
    }
}
