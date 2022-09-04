package com.danakga.webservice.cart.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    //장바구니 아이디
    private Long cartId;
    // 상품아이디
    private Long productId;
    // 상품 수량
    private Integer cartAmount;




}
