package com.danakga.webservice.Cart.service;

import com.danakga.webservice.Cart.dto.request.CartDto;
import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface CartService {
    //장바구니 추가 -> insert
    Long cartPut(UserInfo userInfo, CartDto cartDto);
    //장바구니 목록 -> select
    //List<Cart> cartList();

    //장바구니 비우기 -> delete
    Long cartDelete(Long cartId);
}
