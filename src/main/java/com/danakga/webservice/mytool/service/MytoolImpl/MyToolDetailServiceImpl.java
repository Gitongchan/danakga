package com.danakga.webservice.mytool.service.MytoolImpl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyToolDetailServiceImpl implements MyToolDetailService {
    private final ProductRepository productRepository;
    private final MyToolDetailRepository myToolDetailRepository;
    private final MyToolFolderRepository myToolFolderRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void MyToolDetailSave(UserInfo userInfo,List<DetailSaveDto> detailSaveDto) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        for (DetailSaveDto saveDto : detailSaveDto) {
            Product myToolProduct = productRepository.findByProductId(saveDto.getProductId()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
            );
            MyToolFolder myToolFolder = myToolFolderRepository.findByIdAndUserInfo(saveDto.getFolderId(),detailUserInfo).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
            );

            myToolDetailRepository.save(
                    MyToolDetail.builder()
                            .product(myToolProduct)
                            .myToolFolder(myToolFolder)
                            .myToolQuantity(saveDto.getMyToolQuantity())
                            .build()
            );
        }

    }
}
