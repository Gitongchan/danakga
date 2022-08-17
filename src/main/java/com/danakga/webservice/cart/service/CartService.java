package com.danakga.webservice.cart.service;

import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.dto.request.CartIdDto;
import com.danakga.webservice.cart.dto.response.ResCartDto;
import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface CartService {
    //장바구니 추가
    Long cartSave(UserInfo userInfo, List<CartDto> cartDto);

    void MyCartDelete(UserInfo userInfo, List<CartIdDto> productList);

    void cartDeleteAll(UserInfo userInfo);

    //장바구니 조회
    List<ResCartDto>  cartList(UserInfo userInfo);

}
