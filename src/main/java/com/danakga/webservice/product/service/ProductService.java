package com.danakga.webservice.product.service;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.dto.request.DeletedFileDto;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.dto.request.ProductSearchDto;
import com.danakga.webservice.product.dto.response.ResProductDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {
    
    //상품 등록
    Long productUpload(UserInfo userInfo, ProductDto productDto,MultipartFile thumb,List<MultipartFile> files);

    //상품 목록 조회
    List<ResProductListDto> productList(Pageable pageable,
                                        @RequestBody ProductSearchDto productSearchDto, int page);

    //내가 등록한 상품 목록 조회
    List<ResProductListDto> myProductList(@LoginUser UserInfo userInfo,
                                          LocalDateTime startDate, LocalDateTime endDate,
                                          Pageable pageable,
                                          String productName,
                                          Integer productStock, int page);

    //개별 상품 조회
    ResProductDto productInfo(Long productId, HttpServletRequest request, HttpServletResponse response);
    
    //상품 수정
    Long productUpdate(UserInfo userInfo, Long productId, ProductDto productDto,String deletedThumb,MultipartFile thumb, DeletedFileDto deletedFileDto, List<MultipartFile> files);

    //상품 삭제
    Long productDelete(UserInfo userInfo,Long productId);

    //수정 삭제 버튼 확인
    Long updateDeleteButton(UserInfo userInfo,Long productId);

}
