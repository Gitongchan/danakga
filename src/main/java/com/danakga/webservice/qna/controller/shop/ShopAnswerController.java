package com.danakga.webservice.qna.controller.shop;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.service.ShopAnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ShopAnswerController {

    private final ShopAnswerService shopAnswerService;

    /* 가게 문의사항 답변 작성 */
    @PostMapping("/shop_answer/write/{c_id}/{qn_id}")
    public ResResultDto shopAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqAnswerDto reqAnswerDto,
                                        @PathVariable("c_id") Long c_id,
                                        @PathVariable("qn_id") Long qn_id) {

        return shopAnswerService.shopAnswerWrite(userInfo, reqAnswerDto, c_id, qn_id);
    }
    
    /* 가게 문의사항 답변 수정 */
    @PutMapping("/shop_answer/edit/{c_id}/{qn_id}/{an_id}")
    public ResResultDto shopAnswerEdit(@LoginUser UserInfo userInfo,
                                       @RequestBody ReqAnswerDto reqAnswerDto,
                                       @PathVariable("c_id") Long c_id,
                                       @PathVariable("qn_id") Long qn_id,
                                       @PathVariable("an_id") Long an_id) {

        return shopAnswerService.shopAnswerEdit(userInfo, reqAnswerDto, c_id, qn_id, an_id);
    }

    /* 가게 문의사항 답변 삭제 */
    @PutMapping("/shop_answer/delete/{c_id}/{qn_id}/{an_id}")
    public ResResultDto shopAnswerDelete(@LoginUser UserInfo userInfo,
                                         @PathVariable("c_id") Long c_id,
                                         @PathVariable("qn_id") Long qn_id,
                                         @PathVariable("an_id") Long an_id) {
        
        return shopAnswerService.shopAnswerDelete(userInfo, c_id, qn_id, an_id);
    }
}
