package com.danakga.webservice.qna.site.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.site.dto.request.ReqSiteQnADto;
import com.danakga.webservice.qna.site.service.SiteQnAService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SiteQnAController {

    private final SiteQnAService siteQnAService;

    /* 사이트 문의사항 작성 */
    @PostMapping("/siteQnA/write")
    public ResResultDto siteQnAWrite(@LoginUser UserInfo userInfo,
                                    @RequestBody ReqSiteQnADto reqQnADto) {

        return siteQnAService.siteQnAWrite(userInfo, reqQnADto);
    }

    /* 사이트 문의사항 수정 */
    @PutMapping("/siteQnA/edit/{sq_id}")
    public ResResultDto siteQnAEdit(@LoginUser UserInfo userInfo,
                                @RequestBody ReqSiteQnADto reqQnADto,
                                @PathVariable Long sq_id) {

        return siteQnAService.siteQnAEdit(userInfo, reqQnADto, sq_id);
    }
    
    /* 사이트 문의사항 삭제 상태 변경 */
    @PutMapping("/siteQnA/delete/{sq_id}")
    public ResResultDto siteQnADelete(@LoginUser UserInfo userInfo,
                                      @PathVariable Long sq_id) {

        return siteQnAService.siteQnADelete(userInfo, sq_id);
    }
}
