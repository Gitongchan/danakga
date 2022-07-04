package com.danakga.webservice.Cart.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity //JPA가 관리하는 클래스
@Getter // 게터 자동 삽입
@NoArgsConstructor(access = AccessLevel.PROTECTED)//무분별한 객체 생성에 대해 한번 더 체크할 수 있는 수단

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cart_id;

    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product_id;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    @Column(name = "fie_id")
    private String fieId; // 장바구니명을 받으니 str 형으로 ~

    @Column(name = "cart_amount") // 기본값 1  개수 세는 역할
    private Integer cart_amount;

    @Builder
    public Cart(Long cart_id, Product pd_id, UserInfo u_id, String fieId, Integer cart_amount ){
        this.cart_id = cart_id;
        this.product_id = pd_id;
        this.userInfo = u_id;
        this.fieId = fieId;
        this.cart_amount = cart_amount;
    }

}
