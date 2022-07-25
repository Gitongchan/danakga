package com.danakga.webservice.cart.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.service.CartService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러잉을 명시
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/user")
public class CartController {
    // json 객체는 리퀘스트 바디로
      private final CartService cartService;
    @PostMapping(value = "/cart")
    public ResResultDto cartPut(@LoginUser UserInfo userInfo,@RequestBody CartDto cartDto){
        Long result = cartService.cartPut(userInfo,cartDto);
        return new ResResultDto(result,"장바구니 등록  성공.");

    }
//    @PutMapping(value="/cart")
//    public  ResResultDto cartDelete(@RequestParam Long cartId){
//        Long result = cartService.cartDelete(cartId);
//        return result == -1L ?
//                new ResResultDto(result,"장바구니 삭제 실패.") : new ResResultDto(result,"장바구니 삭제 실패.");
//    }


}
