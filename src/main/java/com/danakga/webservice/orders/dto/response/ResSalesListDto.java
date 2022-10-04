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

    /* 고객 정보 */

    //주문 고객 ID
    private String userId;

    //주문 고객명
    private String userName;

    //주문 고객 핸드폰 번호
    private String userPhone;

    //주문 고객 우편번호
    private String userAdrNum;

    //주문 고객 주소 ( 도로명 주소 + 상세주소 )
    private String userAddress;


    /* 주문 상품 정보 */

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
