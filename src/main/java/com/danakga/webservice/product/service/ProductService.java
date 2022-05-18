package com.danakga.webservice.product.service;

import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;

public interface ProductService {

    public Long productUpload(UserInfo userInfo, ProductDto productDto);
}
