package com.danakga.webservice.product.controller;


import com.danakga.webservice.product.dto.request.ProductSearchDto;
import com.danakga.webservice.product.dto.response.ResProductDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductPermitAllController {
    private final ProductService productService;
    //상품 리스트
    @GetMapping("/list")
    public List<ResProductListDto> productList(Pageable pageable,
                                               @RequestPart(value = "productSearchDto") ProductSearchDto productSearchDto, int page) {
        return productService.productList(pageable, productSearchDto, page);
    }
    //상품 조회
    @GetMapping("/item/{id}")
    public ResProductDto productInfo(@PathVariable("id") Long id, HttpServletRequest request,
                                     HttpServletResponse response
    ) {
        return productService.productInfo(id,request,response);
    }
}
