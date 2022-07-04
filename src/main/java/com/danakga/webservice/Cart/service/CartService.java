package com.danakga.webservice.Cart.service;

import com.danakga.webservice.user.model.UserInfo;

public interface CartService {
    //장바구니 추가 -> insert
    Long cartPut( UserInfo userInfo, Long cart_id, Long pd_id,
                 String fieId,Integer cart_amount);
    //장바구니 목록 -> select
    Long cartList();
    //장바구니 상품 삭제 -> 수량 삭제 -> update
    //장바구니 비우기 -> delete
}
