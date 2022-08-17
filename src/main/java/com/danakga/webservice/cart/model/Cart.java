package com.danakga.webservice.cart.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @Column(name = "cart_amount") // 기본값 1  개수 세는 역할
    private Integer cartAmount;

    @Builder
    public Cart(Long cartId, Product productId, UserInfo userInfo,  Integer cartAmount){
        this.cartId = cartId;
        this.productId = productId;
        this.userInfo = userInfo;
        this.cartAmount = cartAmount;

    }

}
