package com.danakga.webservice.Cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @Getter / @Setter, @ToString, @EqualsAndHashCode와 @RequiredArgsConstructor 를 합쳐놓은 종합 선물
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듬
public class CartDto {
    //장바구니 아이디
    private Long cart_id;
    // 상품아이디
    private Long pd_id;
    //장바구니 명 (상품명인듯)
    private String fieId;
    // 상품 수량
    private Integer cart_amount;
}
