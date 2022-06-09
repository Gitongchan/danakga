package com.danakga.webservice.orders.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.service.OrdersService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("api/user/orders/{productId}")
    public ResResultDto ordersSave(@LoginUser UserInfo userInfo, @PathVariable("productId") Long productId
            ,@RequestBody OrdersDto ordersDto){
        Long result = ordersService.ordersSave(userInfo,productId,ordersDto);
        return result == -1L ? 
                new ResResultDto(result,"재고가 부족합니다.") : new ResResultDto(result,"성공적으로 주문되었습니다.");
    }
}
