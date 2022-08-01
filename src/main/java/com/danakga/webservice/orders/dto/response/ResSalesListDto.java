package com.danakga.webservice.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSalesListDto {
    //주문번호
    private Long ordersId;

    //상품 아이디
    private Long productId;

    //상품브랜드
    private String productBrand;

    //상품명
    private String productName;

    //주문수량
    private int ordersQuantity;

    //주문 금액
    private int ordersPrice;

    //주문 날짜
    private LocalDateTime ordersDate;

    //주문 상태
    private String orderStatus;

    private int totalPage;

    private long totalElement;

}
