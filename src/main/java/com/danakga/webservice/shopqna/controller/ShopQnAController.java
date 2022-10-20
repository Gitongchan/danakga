package com.danakga.webservice.shopqna.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.shopqna.dto.request.ReqShopQnADto;
import com.danakga.webservice.shopqna.service.ShopQnAService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ShopQnAController {

    private final ShopQnAService shopQnAService;

    /* 문의 사항 작성 */
    @PostMapping("/shopQnA/write")
    public ResResultDto shopQnAWrite(@LoginUser UserInfo userInfo,
                                     @RequestBody ReqShopQnADto reqShopQnADto) {

        return shopQnAService.shopQnAWrite(userInfo, reqShopQnADto);
    }

    /* 문의사항 수정 */
    @PutMapping("/shopQnA/edit/{sq_id}")
    public ResResultDto shopQnAEdit(@LoginUser UserInfo userInfo,
                                    @RequestBody ReqShopQnADto reqShopQnADto,
                                    @PathVariable Long sq_id) {

        return shopQnAService.shopQnAEdit(userInfo, reqShopQnADto, sq_id);
    }
    
    /* 문의사항 삭제 상태 변경 */
    @PutMapping("/shopQnA/delete/{sq_id}")
    public ResResultDto shopQnADelete(@LoginUser UserInfo userInfo,
                                      @PathVariable Long sq_id) {

        return shopQnAService.shopQnADelete(userInfo, sq_id);
    }
}
