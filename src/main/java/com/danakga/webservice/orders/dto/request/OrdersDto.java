package com.danakga.webservice.orders.dto.request;

import com.danakga.webservice.orders.model.OrderStatus;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto {
    //주문 아이디
    private Long ordersId;

    //주문한 사용자 정보
    private UserInfo userInfo;

    //주문한 상품 정보
    private Product product;

    //주문 상태
    private OrderStatus orderStatus;

    //주문 날짜
    private LocalDateTime ordersDate;

    //배송 완료 날짜
    private LocalDateTime ordersFinishedDate;

    //주문 수량
    private int ordersQuantity;

    //주문 금액
    private int ordersPrice;

    //운송장 번호
    private String ordersTrackingNum;
}
