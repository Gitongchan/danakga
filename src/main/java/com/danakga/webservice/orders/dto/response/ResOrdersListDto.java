package com.danakga.webservice.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrdersListDto {

    //가게이름
    private String companyName;

    //상품브랜드
    private String productBrand;

    //상품명
    private String productName;
    
    //주문수량
    private int ordersQuantity;

    //주문번호
    private Long ordersId;

    //주문 날짜
    private LocalDateTime ordersDate;

    //배송 완료 날짜
    private LocalDateTime ordersFinishedDate;

    //주문 상태
    private String orderStatus;

    private int totalPage;

    private long totalElement;
}
