package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    
    //상품등록
    @Override
    public Long productUpload(UserInfo userInfo, ProductDto productDto) {
        if(userRepository.findById(userInfo.getId()).isEmpty()) return -1L;
        UserInfo uploadUser = userRepository.findById(userInfo.getId()).get();

        if(companyRepository.findByUserInfo(uploadUser).isEmpty()) return -1L;
        CompanyInfo uploadCompany = companyRepository.findByUserInfo(uploadUser).get();

        return productRepository.save(
                Product.builder()
                        .productCompanyId(uploadCompany)
                        .productBrand(productDto.getProductBrand())
                        .productContent(productDto.getProductContent())
                        .productName(productDto.getProductName())
                        .productPhoto(productDto.getProductPhoto())
                        .productPrice(productDto.getProductPrice())
                        .productState(productDto.getProductState())
                        .productStock(productDto.getProductStock())
                        .productUploadDate(LocalDateTime.now())
                        .productType(productDto.getProductType())
                        .productOrderCount(productDto.getProductOrderCount())
                        .productViewCount(productDto.getProductViewCount())
                        .build()
        ).getProductId();

    }
}
