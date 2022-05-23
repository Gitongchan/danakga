package com.danakga.webservice.product.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/upload")
    public ResResultDto productUpload(@LoginUser UserInfo userInfo,@RequestBody ProductDto productDto){
        Long result = productService.productUpload(userInfo,productDto);
        return result == -1L ?
                new ResResultDto(result,"상품등록 실패.") : new ResResultDto(result,"상품등록 성공.");
    }
}
