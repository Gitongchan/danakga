package com.danakga.webservice.orders.model;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class Orders {

    //주문 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orders_id")
    private Long ordersId;

    //주문한 사용자 정보
    @ManyToOne
    @JoinColumn(name="orders_userInfo")
    private UserInfo userInfo;

    //주문한 상품 정보
    @ManyToOne
    @JoinColumn(name="orders_product")
    private Product product;
    
    //주문 상태
    @Enumerated(value = EnumType.STRING)
    @Column(name="orders_status")
    private OrderStatus orderStatus;
    
    //주문 날짜
    @Column(name="orders_date")
    private LocalDateTime ordersDate;

    //배송 완료 날짜
    @Column(name="orders_finished_date")
    private LocalDateTime ordersFinishedDate;

    //주문 금액
    @Column(name = "orders_price")
    private int ordersPrice;

    //주문 수량
    @Column(name="orders_quantity")
    private int ordersQuantity;

    //운송장 번호
    @Column(name="orders_traking_num")
    private String ordersTrackingNum;

    @Builder
    public Orders(Long ordersId, UserInfo userInfo, Product product, OrderStatus orderStatus,
                  LocalDateTime ordersDate, LocalDateTime ordersFinishedDate, int ordersPrice,int ordersQuantity,
                  String ordersTrackingNum) {
        this.ordersId = ordersId;
        this.userInfo = userInfo;
        this.product = product;
        this.orderStatus = orderStatus;
        this.ordersDate = ordersDate;
        this.ordersFinishedDate = ordersFinishedDate;
        this.ordersPrice = ordersPrice;
        this.ordersQuantity = ordersQuantity;
        this.ordersTrackingNum = ordersTrackingNum;
    }
}




