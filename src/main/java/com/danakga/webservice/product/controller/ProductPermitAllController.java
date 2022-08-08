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
                                               @RequestParam String productType,
                                               @RequestParam String productSubType,
                                               @RequestParam String productBrand,
                                               @RequestParam String productName,
                                               @RequestParam Integer productStock,
                                               @RequestParam int page,@RequestParam String sort,@RequestParam String order) {
        ProductSearchDto productSearchDto = new ProductSearchDto(
                productType,productSubType,productBrand,productName,productStock
        );
        return productService.productList(pageable, productSearchDto, page,sort,order);
    }


    //메인페이지 상품 리스트
    @GetMapping("/main-page/list/{sort}")
    public List<ResProductListDto> productMainPageList(Pageable pageable,
                                                       @PathVariable("sort") String sort,
                                                       int page){
        ProductSearchDto productSearchDto = new ProductSearchDto(
                "%","%","%","%",1
        );
        return productService.productMainPageList(pageable,productSearchDto,page,sort);
    }

    //상품 조회
    @GetMapping("/item/{id}")
    public ResProductDto productInfo(@PathVariable("id") Long id, HttpServletRequest request,
                                     HttpServletResponse response
    ) {
        return productService.productInfo(id,request,response);
    }
}
