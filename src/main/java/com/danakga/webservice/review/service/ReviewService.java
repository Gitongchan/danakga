package com.danakga.webservice.review.service;

import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    /* 후기 목록 */
    ResReviewListDto reviewList(Long p_id, Pageable pageable, int page);

    /* 후기 작성 */
    ResResultDto reviewWrite(ReqReviewDto reqReviewDto, UserInfo userInfo);

    /* 후기 수정 */
    ResResultDto reviewEdit(ReqReviewDto reqReviewDto, UserInfo userInfo, Long re_id);

}
