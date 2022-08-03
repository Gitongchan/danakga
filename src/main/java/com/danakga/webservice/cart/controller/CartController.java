package com.danakga.webservice.cart.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.dto.request.CartIdDto;
import com.danakga.webservice.cart.service.CartService;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 컨트롤러잉을 명시
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/user")
public class CartController {
    // json 객체는 리퀘스트 바디로
      private final CartService cartService;
    @PostMapping(value = "/cart")
    public ResResultDto cartPut(@LoginUser UserInfo userInfo,@RequestBody CartDto cartDto){
        Long result = cartService.cartPut(userInfo,cartDto);
        return new ResResultDto(result,"장바구니 등록 성공.");

    }
    @DeleteMapping(value="/cart/delete")
    public ResResultDto cartDelete(@LoginUser UserInfo userInfo, @RequestBody List<CartIdDto> cartIdDto){
        cartService.MyCartDelete(userInfo,cartIdDto);
        return new ResResultDto(1L,"장바구니에서 삭제하였습니다.");
    }

    // 조회
    // 전체 삭제

    @DeleteMapping(value="/cart/deleteAll")
    public ResResultDto cartDeleteAll(@LoginUser UserInfo userInfo, @RequestBody CartIdDto cartIdDto){
        cartService.cartDeleteAll(userInfo);
        return new ResResultDto(1L,"장바구니에서 삭제하였습니다.");
    }


}