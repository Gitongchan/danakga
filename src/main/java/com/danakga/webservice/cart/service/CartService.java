package com.danakga.webservice.cart.service;

import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;

public interface CartService {
    //장바구니 추가 -> insert
    Long cartPut(UserInfo userInfo, CartDto cartDto);

    //장바구니 목록 -> select
    //List<Cart> cartList(UserInfo userInfo);

    //장바구니 비우기 -> delete
    Long cartDelete(Product productId);

    Long cartDeleteAll(Product productId);

}
