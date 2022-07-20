package com.danakga.webservice.mytool.service.MytoolImpl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
import com.danakga.webservice.mytool.dto.request.MyToolIdDto;
import com.danakga.webservice.mytool.dto.response.ResMyToolDetailDto;
import com.danakga.webservice.mytool.model.MyToolDetail;
import com.danakga.webservice.mytool.model.MyToolFolder;
import com.danakga.webservice.mytool.repository.MyToolDetailRepository;
import com.danakga.webservice.mytool.repository.MyToolFolderRepository;
import com.danakga.webservice.mytool.service.MyToolDetailService;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyToolDetailServiceImpl implements MyToolDetailService {
    private final ProductRepository productRepository;
    private final MyToolDetailRepository myToolDetailRepository;
    private final MyToolFolderRepository myToolFolderRepository;
    private final UserRepository userRepository;

    //장비 목록 저장
    @Transactional
    @Override
    public void MyToolDetailSave(UserInfo userInfo,List<DetailSaveDto> detailSaveDto) {

        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );
        MyToolFolder myToolFolder = myToolFolderRepository.findByIdAndUserInfo(detailSaveDto.get(0).getFolderId(),detailUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
        );


        if(CollectionUtils.isEmpty(myToolDetailRepository.findByMyToolFolder(myToolFolder))){
            for (DetailSaveDto saveDto : detailSaveDto) {
                Product myToolProduct = productRepository.findByProductId(saveDto.getProductId()).orElseThrow(
                        () -> new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
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
        else{
            myToolDetailRepository.deleteAllByMyToolFolder(myToolFolder);

            for (DetailSaveDto saveDto : detailSaveDto) {
                Product myToolProduct = productRepository.findByProductId(saveDto.getProductId()).orElseThrow(
                        () -> new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
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

    //내 장비 목록에 저장된 제품 삭제
    @Transactional
    @Override
    public void MyToolDelete(UserInfo userInfo, MyToolIdDto myToolIdDto) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        MyToolFolder myToolFolder = myToolFolderRepository.findByIdAndUserInfo(myToolIdDto.getMyToolFolderId(),detailUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
        );

        MyToolDetail myToolDetail = myToolDetailRepository.findByIdAndMyToolFolder(myToolIdDto.getMyToolId(),myToolFolder).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("저장된 제품 정보를 찾을 수 없습니다.")
        );
        
        myToolDetailRepository.delete(myToolDetail);

    }

    //내장비 리스트 / 폴더별로 정렬
    @Override
    public List<ResMyToolDetailDto> myToolList(UserInfo userInfo, Long myToolFolderId) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        MyToolFolder myToolFolder = myToolFolderRepository.findByIdAndUserInfo(myToolFolderId,detailUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
        );

        List<MyToolDetail> myToolDetailList = myToolDetailRepository.findByMyToolFolder(myToolFolder);
        
        List<ResMyToolDetailDto> resMyToolDetailDtoList = new ArrayList<>();
        myToolDetailList.forEach(entity->{
                    ResMyToolDetailDto listDto = new ResMyToolDetailDto();
                    listDto.setMyToolFolderId(entity.getMyToolFolder().getId());
                    listDto.setMyToolId(entity.getId());
                    listDto.setMyToolProductId(entity.getProduct().getProductId());
                    listDto.setMyToolProductType(entity.getProduct().getProductType());
                    listDto.setMyToolProductSubType(entity.getProduct().getProductSubType());
                    listDto.setMyToolProductBrand(entity.getProduct().getProductBrand());
                    listDto.setMyToolProductName(entity.getProduct().getProductName());
                    listDto.setMyToolProductPrice(entity.getProduct().getProductPrice());
                    listDto.setMyToolQuantity(entity.getMyToolQuantity());
                    resMyToolDetailDtoList.add(listDto);
                }
        );

        return resMyToolDetailDtoList;
    }
}
