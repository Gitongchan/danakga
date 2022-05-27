package com.danakga.webservice.product.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto productUpload(@LoginUser UserInfo userInfo,
                                      @RequestPart(value = "product") ProductDto productDto,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> files){

        Long result = productService.productUpload(userInfo,productDto,files);
        
        if(result.equals(-1L)){
            return new ResResultDto(result,"상품등록 실패.");
        } 
        else if(result.equals(-2L)){
            return new ResResultDto(result,"상품등록 실패, 이미지 파일 업로드 실패");
        }
        else{
            return new ResResultDto(result,"상품등록 성공.");
        }

    }
}
