package com.danakga.webservice.review.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResReviewListDto {
    
    /* 후기 목록 */
    List<Map<String, Object>> reviewList;
}
