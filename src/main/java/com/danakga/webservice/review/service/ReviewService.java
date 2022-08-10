package com.danakga.webservice.review.service;

import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface ReviewService {

    /* 후기 작성 */
    ResResultDto reviewWrite(ReqReviewDto reqReviewDto, UserInfo userInfo);
    
}
