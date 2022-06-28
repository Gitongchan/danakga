package com.danakga.webservice.mytool.service.MytoolImpl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.model.MyToolDetail;
import com.danakga.webservice.mytool.model.MyToolFolder;
import com.danakga.webservice.mytool.repository.MyToolDetailRepository;
import com.danakga.webservice.mytool.repository.MyToolFolderRepository;
import com.danakga.webservice.mytool.service.MyToolDetailService;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyToolDetailServiceImpl implements MyToolDetailService {
    private final ProductRepository productRepository;
    private final MyToolDetailRepository myToolDetailRepository;
    private final MyToolFolderRepository myToolFolderRepository;


    @Override
    public Long MyToolDetailSave(Long productId, Long folderId) {
        Product myToolProduct = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
        );

        MyToolFolder myToolFolder = myToolFolderRepository.findById(folderId).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
        );

        return myToolDetailRepository.save(
                MyToolDetail.builder()
                        .myToolFolder(myToolFolder)
                        .product(myToolProduct)
                        .build()
        ).getId();

    }
}
