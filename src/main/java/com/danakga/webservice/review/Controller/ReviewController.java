package com.danakga.webservice.review.Controller;


import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.review.dto.request.ReqReviewDeleteDto;
import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.review.service.ReviewService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ReviewController {

    private final ReviewService reviewService;

    /* 후기 작성 체크 */
    @GetMapping("/review/check/{o_id}")
    public ResResultDto reviewCheck(@PathVariable("o_id") Long o_id) {

        return reviewService.reviewCheck(o_id);
    }

    /* 후기 작성 */
    @PostMapping("/review/write")
    public ResResultDto reviewWrite(@Valid @RequestBody ReqReviewDto reqReviewDto,
                                    @LoginUser UserInfo userInfo) {

        return reviewService.reviewWrite(reqReviewDto, userInfo);
    }

    
    /* 후기 수정 */
    @PutMapping("/review/edit/{re_id}")
    public ResResultDto reviewEdit(@Valid @RequestBody ReqReviewDto reqReviewDto,
                                   @PathVariable("re_id") Long re_id,
                                   @LoginUser UserInfo userInfo) {

        return reviewService.reviewEdit(reqReviewDto, userInfo, re_id);
    }
    
    
    /* 후기 삭제 상태 변경 */
    @PutMapping("/review/delete/{re_id}")
    public ResResultDto reviewDelete(@RequestBody ReqReviewDeleteDto reqReviewDeleteDto,
                                     @PathVariable("re_id") Long re_id,
                                     @LoginUser UserInfo userInfo) {

        return reviewService.reviewDelete(reqReviewDeleteDto, userInfo, re_id);
    }
}
