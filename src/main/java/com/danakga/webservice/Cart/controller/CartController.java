package com.danakga.webservice.Cart.controller;

import com.danakga.webservice.Cart.dto.request.CartDto;
import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.Cart.service.CartService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러잉을 명시
@RequiredArgsConstructor // 생성자 주입
public class CartController {
    private final CartService cartService;
    @PostMapping(value = "/cart_put")
    public ResResultDto cartPut(@LoginUser UserInfo userInfo, CartDto cartDto){
        Long result = cartService.cartPut(userInfo,cartDto.getCartId(),cartDto.getPdId(), cartDto.getCartAmount());
        return new ResResultDto(result,"장바구니 등록 완료했습니다.");
    }

    @PostMapping(value="/cart_add")
    public void cartAdd(Cart cart , CartDto cartDto){
        cartService.addItem(cart, cartDto.getCartId());
    }

    @PostMapping(value="/cart_sub")
    public void cartSub(Cart cart , CartDto cartDto){
        cartService.subItem(cart, cartDto.getCartId());
    }

}
