package com.danakga.webservice.cart.service;

import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.dto.request.CartIdDto;
import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface CartService {
    //장바구니 추가 -> insert
    Long cartPut(UserInfo userInfo, CartDto cartDto);

    void MyCartDelete(UserInfo userInfo, List<CartIdDto> productList);


    //장바구니 목록 -> s

    void cartDeleteAll(UserInfo userInfo);

}
