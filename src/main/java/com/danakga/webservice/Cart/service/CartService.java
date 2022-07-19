package com.danakga.webservice.Cart.service;

import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface CartService {
    //장바구니 추가 -> insert
    Long cartPut( UserInfo userInfo, Long cartId, Long productId,Integer cartAmount);
    //장바구니 목록 -> select
    List<Cart> cartList();
    // 수량 증가
    void addItem(Cart cart , Long cartId);
    // 수량 삭제
    void subItem(Cart cart , Long cartId);

    //장바구니 상품 삭제 -> 수량 삭제 -> update

    //장바구니 비우기 -> delete
}
