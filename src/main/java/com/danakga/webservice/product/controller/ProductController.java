package com.danakga.webservice.product.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.dto.request.ProductSearchDto;

import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/product")
public class ProductController {
    @Autowired private final ProductService productService;

    //상품등록
    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto productUpload(@LoginUser UserInfo userInfo,
                                      @RequestPart(value = "keys") ProductDto productDto,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        Long result = productService.productUpload(userInfo, productDto, files);

        if (result.equals(-1L)) {
            return new ResResultDto(result, "상품등록 실패.");
        } else if (result.equals(-2L)) {
            return new ResResultDto(result, "상품등록 실패, 이미지 파일 업로드 실패");
        } else {
            return new ResResultDto(result, "상품등록 성공.");
        }

    }

    //상품 리스트
    @GetMapping("/list")
    public List<ResProductListDto> productList(Pageable pageable,
                                               @RequestPart(value = "productSearchDto") ProductSearchDto productSearchDto, int page) {
        return productService.productList(pageable, productSearchDto, page);
    }

    //상품 수정
    @PutMapping(value = "/update/{item}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto productUpdate(@LoginUser UserInfo userInfo,@PathVariable("item") Long productId,
                                      @RequestPart(value = "keys") ProductDto productDto,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> files){
        Long result = productService.productUpdate(userInfo,productId,productDto,files);
        return result == -1L ?
                new ResResultDto(result,"상품 수정 실패.") : new ResResultDto(result,"상품 수정 성공.");
    }

    //상품 삭제
    @DeleteMapping("/delete/{item}")
    public ResResultDto productDelete(@LoginUser UserInfo userInfo ,@PathVariable("item") Long productId){
        Long result = productService.productDelete(userInfo,productId);

        return result == -1L ?
                new ResResultDto(result,"상품 삭제 실패.") : new ResResultDto(result,"상품 삭제 성공.");
    }

}
