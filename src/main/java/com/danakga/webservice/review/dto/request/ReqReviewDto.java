package com.danakga.webservice.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqReviewDto {

    // Lombok과 RequestBody 사용시 변수명 맨 앞 소문자로 1개만 주면 
    // getoId() 이렇게 인식해서 null값 박힘
    // 소문자 2개 쓰거나 첫글자도 대문자로 써야함
    
    //주문번호
    private Long orderId;

    //상품번호
    private Long productId;
    
    //후기 내용
    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String reviewContent;

    //후기 별점
    private int reviewScore;

}
