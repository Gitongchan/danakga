package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.product.dto.request.ProductDto;
import com.danakga.webservice.product.dto.request.ProductSearchDto;
import com.danakga.webservice.product.dto.response.ResProductDto;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.model.ProductFiles;
import com.danakga.webservice.product.repository.ProductFilesRepository;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.product.service.ProductFilesService;
import com.danakga.webservice.product.service.ProductService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductFilesRepository productFilesRepository;
    private final ProductRepository productRepository;
    private final ProductFilesService productFilesService;
    
    //상품등록
    @Transactional
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
                            .productPhoto("0")
                            .productPrice(productDto.getProductPrice())
                            .productStock(productDto.getProductStock())
                            .productUploadDate(LocalDateTime.now())
                            .productType(productDto.getProductType())
                            .productSubType(productDto.getProductSubType())
                            .productBrand(productDto.getProductBrand())
                            .productOrderCount(0)
                            .productViewCount(0)
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
                                        .productPhoto("0")
                                        .productPrice(productDto.getProductPrice())
                                        .productStock(productDto.getProductStock())
                                        .productUploadDate(LocalDateTime.now())
                                        .productType(productDto.getProductType())
                                        .productSubType(productDto.getProductSubType())
                                        .productBrand(productDto.getProductBrand())
                                        .productOrderCount(0)
                                        .productViewCount(0)
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

    //개별 상품 조회
    @Override
    public ResProductDto productInfo(Long productId, HttpServletRequest request, HttpServletResponse response) {

        if (productRepository.findByProductId(productId).isEmpty()){
            return null;
        }


        Product productInfo = productRepository.findByProductId(productId).get();   //상품정보
        List<ProductFiles> files = productFilesRepository.findByProduct(productInfo);  //상품의 파일정보
        
        Long companyId = productInfo.getProductCompanyId().getCompanyId();

        if(companyRepository.findByCompanyId(companyId).isEmpty()){
            return null; //등록한 회사 정보가 없음
        }
        CompanyInfo companyInfo = companyRepository.findByCompanyId(companyId).get(); //상품을 등록한 회사정보

        //조회수 증가, 쿠키로 중복 증가 방지
        //쿠키가 있으면 그 쿠키가 해당 게시글 쿠키인지 확인하고 아니라면 조회수 올리고 setvalue로 해당 게시글의 쿠키 값도 넣어줘야함
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        //기존에 쿠키를 가지고 있다면 해당 쿠키를 oldCookie에 담아줌
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        //oldCookie가 쿠키를 가지고 있으면 oldCookie의 value값에 현재 게시글의 id가 없다면 조회수 증가
        //그리고 현제 게시글 id를 쿠키에 다시 담아서 보냄
        //쿠키가 없다면 새로 생성 후 조회 수 증가
        if(oldCookie != null) {
            if(!oldCookie.getValue().contains("[" + productId.toString() + "]")) {
                productRepository.updateProductView(productId);
                oldCookie.setValue(oldCookie.getValue() + "[" + productId + "]");
                oldCookie.setMaxAge(60 * 60);
                response.addCookie(oldCookie);
            }
        } else {
            Cookie postCookie = new Cookie("postView", "[" + productId + "]");
            productRepository.updateProductView(productId);
            //쿠키 사용시간 1시간 설정
            postCookie.setMaxAge(60 * 60);
            System.out.println("쿠키 이름 : " + postCookie.getValue());
            response.addCookie(postCookie);
        }

        List<Map<String, Object>> mapFiles = new ArrayList<>();
        ResProductDto resProductDto = new ResProductDto(productInfo,companyInfo);

        files.forEach(entity->{
            Map<String, Object> filesMap = new HashMap<>();
            filesMap.put("file_name",entity.getPfSaveName());
            filesMap.put("file_path",entity.getPfPath());
            mapFiles.add(filesMap);
            }
        );

        resProductDto.setFiles(mapFiles);

        return resProductDto;
    }

    //상품 수정
    @Transactional
    @Override
    public Long productUpdate(UserInfo userInfo, Long productId, ProductDto productDto, List<MultipartFile> files) {

        if (userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).isEmpty()) {
            return -1L;
        }
        if (productRepository.findByProductId(productId).isEmpty()) {
            return -1L;
        }


        UserInfo productUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).get();
        Product productInfo = productRepository.findByProductId(productId).get();
        List<ProductFiles> productFilesList = productFilesRepository.findByProduct(productInfo);

        if (companyRepository.findByUserInfo(productUserInfo).isEmpty()) {
            return -1L;
        }

        CompanyInfo companyInfo = companyRepository.findByUserInfo(productUserInfo).get();


        //db값 담아주기 위한 List
        List<String> saveFilePath = new ArrayList<>();

        for (ProductFiles productFiles : productFilesList) {
            saveFilePath.add(productFiles.getPfPath());
        }

        //db에서 파일 정보 제거 + 폴더에서 파일 제거
        for (String filePath : saveFilePath) {
            File deleteFile = new File(filePath);
            if (deleteFile.delete()) {
                productFilesRepository.deleteByProductAndPfPath(productInfo, filePath);
            }
        }

        File deleteThumbNailFile = new File(productInfo.getProductPhoto());
        if (deleteThumbNailFile.delete()) {
            productRepository.deleteProductPhoto(productInfo.getProductId());
        }

        if (CollectionUtils.isEmpty(files)) {
            productRepository.save(
                    Product.builder()
                            .productId(productId)
                            .productCompanyId(companyInfo)
                            .productContent(productDto.getProductContent())
                            .productName(productDto.getProductName())
                            .productPhoto(productInfo.getProductPhoto())
                            .productPrice(productDto.getProductPrice())
                            .productStock(productDto.getProductStock())
                            .productUploadDate(productInfo.getProductUploadDate())
                            .productType(productDto.getProductType())
                            .productSubType(productDto.getProductSubType())
                            .productBrand(productDto.getProductBrand())
                            .productOrderCount(productInfo.getProductOrderCount())
                            .productViewCount(productInfo.getProductViewCount())
                            .build()
            );
            return productId;
        } else if (!CollectionUtils.isEmpty(files)) {

            for (MultipartFile multipartFile : files) {
                //확장자 체크 위해서 게시글 원본 이름 가져옴
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {

                    Product product = productRepository.save(
                            Product.builder()
                                    .productId(productId)
                                    .productCompanyId(companyInfo)
                                    .productContent(productDto.getProductContent())
                                    .productName(productDto.getProductName())
                                    .productPhoto(productInfo.getProductPhoto())
                                    .productPrice(productDto.getProductPrice())
                                    .productStock(productDto.getProductStock())
                                    .productUploadDate(productInfo.getProductUploadDate())
                                    .productType(productDto.getProductType())
                                    .productSubType(productDto.getProductSubType())
                                    .productBrand(productDto.getProductBrand())
                                    .productOrderCount(productInfo.getProductOrderCount())
                                    .productViewCount(productInfo.getProductViewCount())
                                    .build()
                    );

                    Long result = productFilesService.uploadFile(files, product);

                    if (result.equals(-1L)) {
                        return -2L; //파일업로드 실패하면 -2L반환
                    }
                    return productId;
                }
            }
        }
        return -1L;
    }

    //상품삭제
    @Transactional
    @Override
    public Long productDelete(UserInfo userInfo, Long productId) {

        //유저 정보 + 매니저인지 검증
        if (userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).isEmpty()) {
            return -1L;
        }
        UserInfo productDeleteUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).get();

        //상품을 등록한 회사 정보 검증
       if(companyRepository.findByUserInfo(productDeleteUserInfo).isEmpty()){
            return -1L;
       }
       CompanyInfo productDeleteCompanyInfo =companyRepository.findByUserInfo(productDeleteUserInfo).get();

       //상품번호와 등록환 회사정보 일치하는지 검증
       if(productRepository.findByProductIdAndProductCompanyId(productId,productDeleteCompanyInfo).isEmpty()){
           return -1L;
       }
       Product deleteProduct = productRepository.findByProductIdAndProductCompanyId(productId,productDeleteCompanyInfo).get();

        // 등록된 사진이 있다면 지워줌
        List<ProductFiles> productFilesList = productFilesRepository.findByProduct(deleteProduct);
        if(!productFilesList.isEmpty()){
            //db값 담아주기 위한 List
            List<String> saveFilePath = new ArrayList<>();

            for (ProductFiles productFiles : productFilesList) {
                saveFilePath.add(productFiles.getPfPath());
            }

            //db에서 파일 정보 제거 + 폴더에서 파일 제거
            for (String filePath : saveFilePath) {
                File deleteFile = new File(filePath);
                if (deleteFile.delete()) {
                    productFilesRepository.deleteByProductAndPfPath(deleteProduct, filePath);
                }
            }

            File deleteThumbNailFile = new File(deleteProduct.getProductPhoto());
            if (deleteThumbNailFile.delete()) {
                System.out.println("썸네일 삭제 완료");
            }
        }
        
        //상품을 지워줌
        productRepository.deleteByProductIdAndProductCompanyId(productId,productDeleteCompanyInfo);


        return deleteProduct.getProductId();
    }

}

