package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.dto.request.ProductSearchDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.product.service.ProductFilesService;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    //상품 리스트
    @Override
    public List<ResProductListDto> productList(Pageable pageable, ProductSearchDto productSearchDto, int page) {

        pageable = PageRequest.of(page, 10, Sort.by("productId").descending());

        Page<Product> productPage = productRepository.findByProductTypeLikeAndProductSubTypeLikeAndProductBrandLikeAndProductNameLikeAndProductStockGreaterThanEqual(
                productSearchDto.getProductType(),productSearchDto.getProductSubType(),productSearchDto.getProductBrand(),
                productSearchDto.getProductName(),productSearchDto.getProductStock(),pageable
        );
        List<Product> productList = productPage.getContent();

        List<ResProductListDto> productListDto = new ArrayList<>();

        productList.forEach(entity->{
            ResProductListDto listDto = new ResProductListDto();
            listDto.setProductId(entity.getProductId());
            listDto.setProductBrand(entity.getProductBrand());
            listDto.setProductType(entity.getProductType());
            listDto.setProductSubType(entity.getProductSubType());
            listDto.setProductName(entity.getProductName());
            listDto.setProductPhoto(entity.getProductPhoto());
            listDto.setProductPrice(entity.getProductPrice());
            listDto.setProductStock(entity.getProductStock());
            listDto.setProductViewCount(entity.getProductViewCount());
            listDto.setProductOrderCount(entity.getProductOrderCount());
            listDto.setProductUploadDate(entity.getProductUploadDate());
            listDto.setTotalPage(productPage.getTotalPages());
            listDto.setTotalElement(productPage.getTotalElements());
            productListDto.add(listDto);
        });
        return productListDto;
    }
}

