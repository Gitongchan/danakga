package com.danakga.webservice.orders.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {

    @PostMapping("api/user/orders/save")
    public ResResultDto ordersSave(@LoginUser UserInfo userInfo, Long productId, OrdersDto ordersDto){
        return null;
    }
}
