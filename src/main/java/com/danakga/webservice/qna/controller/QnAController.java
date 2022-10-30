package com.danakga.webservice.qna.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqQnADto;
import com.danakga.webservice.qna.service.QnAService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class QnAController {

    private final QnAService qnaService;

    /* 사이트 문의사항 작성 */
    @PostMapping("/QnA/write")
    public ResResultDto qnaWrite(@LoginUser UserInfo userInfo,
                                 @RequestBody ReqQnADto reqQnADto) {

        return qnaService.qnaWrite(userInfo, reqQnADto);
    }

    /* 사이트 문의사항 수정 */
    @PutMapping("/QnA/edit/{q_id}")
    public ResResultDto qnaEdit(@LoginUser UserInfo userInfo,
                                @RequestBody ReqQnADto reqQnADto,
                                @PathVariable Long q_id) {

        return qnaService.qnaEdit(userInfo, reqQnADto, q_id);
    }
    
    /* 사이트 문의사항 삭제 상태 변경 */
    @PutMapping("/QnA/delete/{q_id}")
    public ResResultDto qnaDelete(@LoginUser UserInfo userInfo,
                                  @PathVariable Long q_id) {

        return qnaService.qnaDelete(userInfo, q_id);
    }
}
