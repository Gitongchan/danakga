package com.danakga.webservice.siteqna.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.siteqna.dto.request.ReqSiteQnADto;
import com.danakga.webservice.siteqna.service.SiteQnAService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SiteQnAController {

    private final SiteQnAService siteQnAService;

    /* 문의 사항 작성 */
    @PostMapping("/siteQnA/write")
    public ResResultDto siteQnAWrite(@LoginUser UserInfo userInfo,
                                     @RequestBody ReqSiteQnADto reqInquiryDto) {

        return siteQnAService.siteQnAWrite(userInfo, reqInquiryDto);
    }

    /* 문의사항 수정 */
    @PutMapping("/siteQnA/edit/{sq_id}")
    public ResResultDto siteQnAEdit(@LoginUser UserInfo userInfo,
                                    @RequestBody ReqSiteQnADto reqInquiryDto,
                                    @PathVariable Long sq_id) {

        return siteQnAService.siteQnAEdit(userInfo, reqInquiryDto, sq_id);
    }
    
    /* 문의사항 삭제 상태 변경 */
    @PutMapping("/siteQnA/delete/{sq_id}")
    public ResResultDto siteQnADelete(@LoginUser UserInfo userInfo,
                                      @PathVariable Long sq_id) {

        return siteQnAService.siteQnADelete(userInfo, sq_id);
    }
}
