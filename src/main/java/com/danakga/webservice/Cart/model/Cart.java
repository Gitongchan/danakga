package com.danakga.webservice.Cart.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.*;

import javax.persistence.*;

@Entity //JPA가 관리하는 클래스
@Getter // 게터 자동 삽입
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//무분별한 객체 생성에 대해 한번 더 체크할 수 있는 수단

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    private Integer price;

    @Column(name = "cart_amount") // 기본값 1  개수 세는 역할s
    private Integer cartAmount;

    @Builder
    public Cart(Long cartId, Product productId, UserInfo userInfo,  Integer cartAmount, Integer price ){
        this.cartId = cartId;
        this.productId = productId;
        this.userInfo = userInfo;
        this.cartAmount = cartAmount;
        this.price = price;
    }

}
