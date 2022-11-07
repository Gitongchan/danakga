package com.danakga.webservice.qna.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.qna.dto.request.ReqQnaDto;
import com.danakga.webservice.qna.service.QnaService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class QnaController {

    private final QnaService qnaService;

    /* 문의사항 작성 */
    /* /qna/write/{c_id} 가게 문의사항 작성 --- /qna/write 사이트 문의사항 작성 */
    @PostMapping({"/qna/product_write/{c_id}", "/qna/site_write"})
    public ResResultDto qnaWrite(@LoginUser UserInfo userInfo,
                                 @RequestBody ReqQnaDto reqQnaDto,
                                 @PathVariable(value = "c_id", required = false) Long c_id) {

        return qnaService.qnaWrite(userInfo, reqQnaDto, c_id);
    }

    /* 문의사항 수정 */
    @PutMapping("/qna/edit/{qn_id}")
    public ResResultDto qnaEdit(@LoginUser UserInfo userInfo,
                                @RequestBody ReqQnaDto reqQnaDto,
                                @PathVariable(value ="qn_id") Long qn_id) {

        return qnaService.qnaEdit(userInfo, reqQnaDto, qn_id);
    }
    
    /* 문의사항 삭제 상태 변경 */
    @PutMapping("/qna/delete/{qn_id}")
    public ResResultDto qnaDelete(@LoginUser UserInfo userInfo,
                                  @PathVariable Long qn_id) {

        return qnaService.qnaDelete(userInfo, qn_id);
    }
}
