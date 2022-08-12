package com.danakga.webservice.review.Controller;


import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.review.service.ReviewService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
