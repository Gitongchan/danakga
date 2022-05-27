package com.danakga.webservice.product.service;

import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Long productUpload(UserInfo userInfo, ProductDto productDto, List<MultipartFile> files);
}
