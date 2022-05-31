package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.model.ProductFiles;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.product.service.ProductFilesService;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final ProductFilesService productFilesService;
    
    //상품등록
    @Override
    public Long productUpload(UserInfo userInfo, ProductDto productDto, List<MultipartFile> files) {
        if(userRepository.findById(userInfo.getId()).isEmpty()) return -1L;
        UserInfo uploadUser = userRepository.findById(userInfo.getId()).get();

        if(companyRepository.findByUserInfo(uploadUser).isEmpty()) return -1L;
        CompanyInfo uploadCompany = companyRepository.findByUserInfo(uploadUser).get();

        if(CollectionUtils.isEmpty(files)) {
            return productRepository.save(
                    Product.builder()
                            .productCompanyId(uploadCompany)
                            .productContent(productDto.getProductContent())
                            .productName(productDto.getProductName())
                            .productPhoto(null)
                            .productPrice(productDto.getProductPrice())
                            .productState(productDto.getProductState())
                            .productStock(productDto.getProductStock())
                            .productUploadDate(LocalDateTime.now())
                            .productType(productDto.getProductType())
                            .productSubType(productDto.getProductSubType())
                            .productBrand(productDto.getProductBrand())
                            .productOrderCount(productDto.getProductOrderCount())
                            .productViewCount(productDto.getProductViewCount())
                            .build()
            ).getProductId();
        }
        else if(!CollectionUtils.isEmpty(files)) {

            for(MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (!CollectionUtils.isEmpty(files)) {
                    if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {

                        Product product = productRepository.save(
                                Product.builder()
                                        .productCompanyId(uploadCompany)
                                        .productContent(productDto.getProductContent())
                                        .productName(productDto.getProductName())
                                        .productPhoto(null)
                                        .productPrice(productDto.getProductPrice())
                                        .productState(productDto.getProductState())
                                        .productStock(productDto.getProductStock())
                                        .productUploadDate(LocalDateTime.now())
                                        .productType(productDto.getProductType())
                                        .productSubType(productDto.getProductSubType())
                                        .productBrand(productDto.getProductBrand())
                                        .productOrderCount(productDto.getProductOrderCount())
                                        .productViewCount(productDto.getProductViewCount())
                                        .build()
                        );

                        Long result = productFilesService.uploadFile(files, product);

                        if(result.equals(-1L)){
                            return -2L; //파일업로드 실패하면 -2L반환
                        }

                        return product.getProductId();
                    }
                }
            }
        }
        return -1L; //상품업로드 실패시 -1L반환
    }
}

