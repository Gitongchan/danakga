package com.danakga.webservice.product.service;

import com.danakga.webservice.board.dto.response.ResBoardPostDto;
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
import java.util.List;

public interface ProductService {
    
    //상품 등록
    Long productUpload(UserInfo userInfo, ProductDto productDto, List<MultipartFile> files);

    //상품 목록 조회
    List<ResProductListDto> productList(Pageable pageable,
                                        @RequestBody ProductSearchDto productSearchDto, int page);

    //개별 상품 조회
    ResProductDto productInfo(Long productId, HttpServletRequest request, HttpServletResponse response);
}
