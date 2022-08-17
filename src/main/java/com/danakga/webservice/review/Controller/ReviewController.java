package com.danakga.webservice.review.Controller;


import com.danakga.webservice.annotation.LoginUser;
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
    public ResResultDto reviewDelete(@PathVariable("re_id") Long re_id,
                                     @LoginUser UserInfo userInfo) {

        return reviewService.reviewDelete(userInfo, re_id);
    }
}
