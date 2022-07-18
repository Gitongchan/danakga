package com.danakga.webservice.product.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.dto.request.DeletedFileDto;
import com.danakga.webservice.product.dto.request.ProductDto;

import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/product")
public class ProductController {
    private final ProductService productService;

    //상품등록
    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto productUpload(@LoginUser UserInfo userInfo,
                                      @RequestPart(value = "keys") ProductDto productDto,
                                      @RequestPart(value = "thumb",required = false) MultipartFile thumb,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        if(thumb.isEmpty()){
            return new ResResultDto(-2L, "상품등록 실패, 대표사진을 등록해주세요.");
        }

        Long result = productService.productUpload(userInfo, productDto, thumb,files);

        if (result.equals(-1L)) {
            return new ResResultDto(result, "상품등록 실패.");
        } else {
            return new ResResultDto(result, "상품등록 성공.");
        }

    }

    //상품 수정
    @PutMapping(value = "/update/{item}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto productUpdate(@LoginUser UserInfo userInfo,
                                      @PathVariable("item") Long productId,
                                      @RequestPart(value = "keys") ProductDto productDto,
                                      @RequestPart(value = "deletedThumb") String deletedThumb,
                                      @RequestPart(value = "thumbFile") MultipartFile thumb,
                                      @RequestPart(value = "deletedFileList",required = false) DeletedFileDto deletedFileDto,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> files){

        //대표사진을 삭제만 하는건 불가능하다.
        if(deletedThumb != null && thumb == null){
            return new ResResultDto(-1L,"상품 대표사진을 등록해주세요.");
        }

        Long result = productService.productUpdate(userInfo,productId,productDto,deletedThumb,thumb,deletedFileDto,files);

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

    //상품 수정,삭제 버튼 활성화
    @GetMapping("/updateBtnCheck/{item}")
    public ResResultDto updateButton(@LoginUser UserInfo userInfo,@PathVariable("item") Long productId){
        Long result =productService.updateDeleteButton(userInfo,productId); //0 이상이면 활성화
        return result == -1L ?
                new ResResultDto(result,"버튼 비활성화.") : new ResResultDto(result,"버튼 활성화.");
    }

    //내가 등록한 상품 리스트
    @GetMapping("/list")
    public List<ResProductListDto> myProductList(@LoginUser UserInfo userInfo, Pageable pageable,
                                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                 @RequestParam("productName") String productName,
                                                 @RequestParam("productStock") Integer productStock,int page) {

        return productService.myProductList(userInfo,startDate,endDate,pageable,productName,productStock,page);
    }


}
