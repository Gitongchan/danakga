package com.danakga.webservice.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqReviewDeleteDto {

    //주문번호
    private Long orderId;

    //상품번호
    private Long productId;
}
