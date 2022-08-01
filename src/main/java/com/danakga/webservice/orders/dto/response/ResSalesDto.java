package com.danakga.webservice.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResSalesDto {
    //주문번호
    private Long ordersId;

    //주문 고객 아이디
    private String userId;

    //주문 고객명
    private String userName;

    //주문 고객 핸드폰 번호
    private String userPhone;

    //주문 고객 우편번호
    private String userAdrNum;

    //주문 고객 도로명 주소
    private String userStreetAdr;

    //주문 고객 지번 주소
    private String userLotAdr;

    //주문 고객 상세 주소
    private String userDetailAdr;


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

    //배송 완료 날짜
    private LocalDateTime ordersFinishedDate;

    //운송장 번호
    private String ordersTrackingNum;

}
