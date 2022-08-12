package com.danakga.webservice.review.Controller;


import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewPermitAllController {

    private final ReviewService reviewService;

    /* 상품 후기 목록 */
    @GetMapping("/reviewList/{p_id}")
    public ResReviewListDto reviewList(@PathVariable("p_id") Long p_id,
                                       Pageable pageable,
                                       int page) {

        return reviewService.reviewList(p_id, pageable, page);
    }
}
