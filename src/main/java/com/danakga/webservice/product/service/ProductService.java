package com.danakga.webservice.product.service;

import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.dto.request.ProductSearchDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Long productUpload(UserInfo userInfo, ProductDto productDto, List<MultipartFile> files);

    List<ResProductListDto> productList(Pageable pageable,
                                        @RequestBody ProductSearchDto productSearchDto, int page);

}
