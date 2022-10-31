package com.danakga.webservice.qna.controller.shop;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.dto.request.ReqQnADto;
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
    @PostMapping("/shopAnswer/write")
    public ResResultDto shopAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqAnswerDto reqAnswerDto) {

        return shopAnswerService.shopAnswerWrite(userInfo, reqAnswerDto);
    }
    
    /* 가게 문의사항 답변 수정 */
    @PostMapping("/shopAnswer/edit/{q_id}")
    public ResResultDto shopAnswerEdit(@LoginUser UserInfo userInfo,
                                       @RequestBody ReqQnADto reqQnADto,
                                       @PathVariable("q_id") Long p_id) {

        return shopAnswerService.shopAnswerEdit(userInfo, reqQnADto, p_id);
    }
}
