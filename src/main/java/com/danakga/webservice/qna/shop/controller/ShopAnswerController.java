package com.danakga.webservice.qna.shop.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.site.dto.request.ReqSiteAnswerDto;
import com.danakga.webservice.qna.site.service.SiteAnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ShopAnswerController {

    private final SiteAnswerService answerService;

    /* 가게 문의사항 답변 작성 */
    @PostMapping("/shopAnswer/write")
    public ResResultDto shopAnswerWrite(@LoginUser UserInfo userInfo,
                                        @RequestBody ReqSiteAnswerDto reqAnswerDto) {

        return answerService.siteAnswerWrite(userInfo, reqAnswerDto);
    }
}
