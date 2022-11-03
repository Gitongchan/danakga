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
    @PostMapping("/shopAnswer/write/{q_id}")
    public ResResultDto shopAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqAnswerDto reqAnswerDto,
                                        @PathVariable("q_id") Long q_id) {

        return shopAnswerService.shopAnswerWrite(userInfo, reqAnswerDto, q_id);
    }
    
    /* 가게 문의사항 답변 수정 */
    @PutMapping("/shopAnswer/edit/{q_id}/{a_id}")
    public ResResultDto shopAnswerEdit(@LoginUser UserInfo userInfo,
                                       @RequestBody ReqAnswerDto reqAnswerDto,
                                       @PathVariable("q_id") Long q_id,
                                       @PathVariable("a_id") Long a_id) {

        return shopAnswerService.shopAnswerEdit(userInfo, reqAnswerDto, q_id, a_id);
    }

    /* 가게 문의사항 답변 삭제 */
    @PutMapping("/shopAnswer/delete/{q_id}/{a_id}")
    public ResResultDto shopAnswerDelete(@LoginUser UserInfo userInfo,
                                         @PathVariable("q_id") Long q_id,
                                         @PathVariable("a_id") Long a_id) {
        
        return shopAnswerService.shopAnswerDelete(userInfo, q_id, a_id);
    }
}
