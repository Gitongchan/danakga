package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.dto.request.DeletedFileDto;
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
import com.danakga.webservice.wishList.repository.WishRepository;
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
    private final WishRepository wishRepository;
    
    //????????????
    @Transactional
    @Override
    public Long productUpload(UserInfo userInfo, ProductDto productDto,MultipartFile thumb,List<MultipartFile> files) {
        UserInfo uploadUser = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("????????? ????????? ?????? ??? ????????????.")
        );

        CompanyInfo uploadCompany = companyRepository.findByUserInfo(uploadUser).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        );

        String thumbFilePath = null;
        //????????? ??????
        if(!thumb.isEmpty()){
            thumbFilePath = productFilesService.thumbFile(thumb);
        }


        if(!CollectionUtils.isEmpty(files)){
            for(MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List??? ?????? ????????? saveFileUpload ??????
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {

                    Product product = productRepository.save(
                            Product.builder()
                                    .productCompanyId(uploadCompany)
                                    .productContent(productDto.getProductContent())
                                    .productName(productDto.getProductName())
                                    .productPhoto(thumbFilePath)
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

                    if (productFilesService.uploadFile(files, product).equals(-1L)) {
                        return -2L; //??????????????? ???????????? -2L??????
                    }
                    return product.getProductId();
                }
            }
        }
        else if(CollectionUtils.isEmpty(files)){
            return productRepository.save(
                    Product.builder()
                            .productCompanyId(uploadCompany)
                            .productContent(productDto.getProductContent())
                            .productName(productDto.getProductName())
                            .productPhoto(thumbFilePath)
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
        return -1L; //??????????????? ????????? -1L??????
    }

    //?????? ?????????
    @Override
    public List<ResProductListDto> productList(Pageable pageable, ProductSearchDto productSearchDto, int page) {

        pageable = PageRequest.of(page, 10, Sort.by("productId").descending());

        Page<Product> productPage = productRepository.searchProductList(
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
            listDto.setProductWishCount(wishRepository.countByProductId(entity));
            listDto.setProductUploadDate(entity.getProductUploadDate());
            listDto.setTotalPage(productPage.getTotalPages());
            listDto.setTotalElement(productPage.getTotalElements());
            productListDto.add(listDto);
        });
        return productListDto;
    }

    //?????? ????????? - ???????????????
    @Override
    public List<ResProductListDto> productMainPageList(Pageable pageable, ProductSearchDto productSearchDto, int page,String sort) {

        pageable = PageRequest.of(page, 8, Sort.by(sort).descending());

        Page<Product> productPage = productRepository.searchProductList(
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


    //?????? ?????? ??????
    @Override
    public ResProductDto productInfo(Long productId, HttpServletRequest request, HttpServletResponse response) {

        Product productInfo = productRepository.findByProductId(productId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        ) ;  //????????????

        List<ProductFiles> files = productFilesRepository.findByProduct(productInfo);  //????????? ????????????

        Long companyId = productInfo.getProductCompanyId().getCompanyId();

        CompanyInfo companyInfo = companyRepository.findByCompanyId(companyId).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        ); //????????? ????????? ????????????

        //????????? ??????, ????????? ?????? ?????? ??????
        //????????? ????????? ??? ????????? ?????? ????????? ???????????? ???????????? ???????????? ????????? ????????? setvalue??? ?????? ???????????? ?????? ?????? ???????????????
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        //????????? ????????? ????????? ????????? ?????? ????????? oldCookie??? ?????????
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        //oldCookie??? ????????? ????????? ????????? oldCookie??? value?????? ?????? ???????????? id??? ????????? ????????? ??????
        //????????? ?????? ????????? id??? ????????? ?????? ????????? ??????
        //????????? ????????? ?????? ?????? ??? ?????? ??? ??????
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
            //?????? ???????????? 1?????? ??????
            postCookie.setMaxAge(60 * 60);
            System.out.println("?????? ?????? : " + postCookie.getValue());
            response.addCookie(postCookie);
        }

        List<Map<String, Object>> mapFiles = new ArrayList<>();

        Long productWishCount = wishRepository.countByProductId(productInfo);
        ResProductDto resProductDto = new ResProductDto(productInfo,companyInfo,productWishCount);

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

    //?????? ??????
    @Transactional
    @Override
    public Long productUpdate(UserInfo userInfo, Long productId, ProductDto productDto,String deletedThumb,
                              MultipartFile thumb,DeletedFileDto deletedFileDto, List<MultipartFile> files) {


        UserInfo productUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("????????? ???????????? ?????? ??? ????????????.")
        );

        //???????????? ????????? ?????? ?????? ??????
        CompanyInfo companyInfo =companyRepository.findByUserInfo(productUserInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("???????????? ?????? ????????? ?????? ??? ????????????.")
        );

        //???????????? ??????????????????
        if(deletedThumb != null && thumb != null){
            File deletedThumbFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\product_thumbNail\\" + deletedThumb);
            if(deletedThumbFile.delete()){
                String updateThumb = productFilesService.thumbFile(thumb);
                productRepository.updateProductMainPhoto(updateThumb,productId);
            }
        }

        //??????????????? ????????? ???????????? ??????????????? ??????
        Product productInfo = productRepository.findByProductIdAndProductCompanyId(productId,companyInfo).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("??????????????? ?????? ??? ????????????.")
        );

        if(deletedFileDto != null){
            //?????? ???????????? ???????????? ?????? List
            List<String> deletedFileNameList = new ArrayList<>();
            //dto?????? ?????? ???????????? ???????????? List, Map
            List<Map<String, Object>> deletedFileNameMap = deletedFileDto.getDeletedFileList();

            //List<Map> ?????? 1??? ??????????????? List<String>??? ?????????
            for (Map<String, Object> stringObjectMap : deletedFileNameMap) {
                deletedFileNameList.add(stringObjectMap.get("value").toString());
            }

            //List<String>?????? ??????????????? ????????? ?????? ??????
            for(String deleteFile : deletedFileNameList) {
                File deletedFiles = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\product_files\\" + deleteFile);
                if(deletedFiles.delete()){
                    productFilesRepository.deleteByProductAndPfSaveName(productInfo, deleteFile);
                }
            }

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
                //????????? ?????? ????????? ????????? ?????? ?????? ?????????
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
                        return -2L; //??????????????? ???????????? -2L??????
                    }
                    return productId;
                }
            }
        }
        return -1L;
    }

    //????????????
    @Transactional
    @Override
    public Long productDelete(UserInfo userInfo, Long productId) {

        UserInfo productDeleteUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("????????? ????????? ?????? ??? ????????????.")
        );

        //????????? ????????? ?????? ?????? ??????
       CompanyInfo productDeleteCompanyInfo =companyRepository.findByUserInfo(productDeleteUserInfo).orElseThrow(
               () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
       );

       //??????????????? ????????? ???????????? ??????????????? ??????
       Product deleteProduct = productRepository.findByProductIdAndProductCompanyId(productId,productDeleteCompanyInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        );

        // ????????? ????????? ????????? ?????????
        List<ProductFiles> productFilesList = productFilesRepository.findByProduct(deleteProduct);
        if(!productFilesList.isEmpty()){
            //db??? ???????????? ?????? List
            List<String> saveFilePath = new ArrayList<>();

            for (ProductFiles productFiles : productFilesList) {
                saveFilePath.add(productFiles.getPfPath());
            }

            //db?????? ?????? ?????? ?????? + ???????????? ?????? ??????
            for (String filePath : saveFilePath) {
                File deleteFile = new File(filePath);
                if (deleteFile.delete()) {
                    productFilesRepository.deleteByProductAndPfPath(deleteProduct, filePath);
                }
            }

            File deleteThumbNailFile = new File(deleteProduct.getProductPhoto());
            if (deleteThumbNailFile.delete()) {
                System.out.println("????????? ?????? ??????");
            }
        }
        
        //????????? ?????????
        productRepository.deleteByProductIdAndProductCompanyId(productId,productDeleteCompanyInfo);


        return deleteProduct.getProductId();
    }

    //?????? ?????? ?????? ??????
    @Override
    public Long updateDeleteButton(UserInfo userInfo, Long productId) {
        //?????? ?????? + ??????????????? ??????
        UserInfo btnUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("????????? ????????? ?????? ??? ????????????.")
        );

        //???????????? ????????? ?????? ?????? ??????
        CompanyInfo CompanyInfo =companyRepository.findByUserInfo(btnUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        );

        //??????????????? ????????? ???????????? ??????????????? ??????
        Product productInfo = productRepository.findByProductIdAndProductCompanyId(productId,CompanyInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("?????? ????????? ?????? ??? ????????????.")
        );


        return productInfo.getProductId();
    }

    //?????? ????????? ?????? ?????????
    @Override
    public List<ResProductListDto> myProductList(UserInfo userInfo, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable,
                                                 String productName,Integer productStock, int page) {
        UserInfo checkUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("????????? ????????? ????????????.")
        );
        CompanyInfo companyInfo = companyRepository.findByUserInfo(checkUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("????????? ?????? ????????? ????????????.")
        );

        pageable = PageRequest.of(page, 10, Sort.by("productId").descending());

        Page<Product> productPage = productRepository.searchMyProductList(
                productName,productStock,
                startDate,endDate,companyInfo,pageable
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

