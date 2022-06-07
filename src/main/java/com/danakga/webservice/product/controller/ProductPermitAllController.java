package com.danakga.webservice.product.controller;


import com.danakga.webservice.product.dto.response.ResProductDto;
import com.danakga.webservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductPermitAllController {
    private final ProductService productService;

    //상품 조회
    @GetMapping("/item/{id}")
    public ResProductDto productInfo(@PathVariable("id") Long id, HttpServletRequest request,
                                     HttpServletResponse response
    ) {
        return productService.productInfo(id,request,response);
    }
}
