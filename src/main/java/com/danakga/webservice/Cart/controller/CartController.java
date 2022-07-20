package com.danakga.webservice.Cart.controller;

import com.danakga.webservice.Cart.dto.request.CartDto;
import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.Cart.service.CartService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러잉을 명시
@RequiredArgsConstructor // 생성자 주입
public class CartController {
    private final CartService cartService;
    @PostMapping(value = "/cart_put")
    public ResResultDto cartPut(@LoginUser UserInfo userInfo, CartDto cartDto){
        Long result = cartService.cartPut(userInfo,cartDto);
        return result == -1L ?
                new ResResultDto(result,"장바구니 등록 실패.") : new ResResultDto(result,"장바구니 등록 성공.");

    }
    @PostMapping(value="/cart_delete")
    public  ResResultDto cartDelete(@RequestParam Long cartId){
        Long result = cartService.cartDelete(cartId);
        return result == -1L ?
                new ResResultDto(result,"장바구니 삭제 실패.") : new ResResultDto(result,"장바구니 삭제 실패.");
    }


}
