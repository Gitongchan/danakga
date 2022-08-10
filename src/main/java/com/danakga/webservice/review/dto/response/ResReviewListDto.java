package com.danakga.webservice.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResReviewListDto {
    
    //후기 목록
    List<Map<String, Object>> test1;
}
