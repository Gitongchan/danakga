package com.danakga.webservice.wishList.service.Impl;

import com.danakga.webservice.exception.CustomException;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.wishList.dto.request.WishIdDto;
import com.danakga.webservice.wishList.dto.response.ResWishListDto;
import com.danakga.webservice.wishList.model.Wish;
import com.danakga.webservice.wishList.repository.WishRepository;
import com.danakga.webservice.wishList.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // 오토와이드가 필요 없음
public class WishServiceImpl implements WishService {
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

// 유저가 프러덕트 아이가 있으면 삭제 -> 레퍼지토리 생성
    @Override
    public Long wishProcess(UserInfo userInfo, Long productId) {
        //검증을 해줘야함
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow( // 객체의 아이디 값을 조회
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product productInfo = productRepository.findByProductId(productId).orElseThrow( // 파라미터로 long 타입을 갖기 때문
                ()->new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다")
        );
        if(wishRepository.findByUserInfoAndProductId(wishUserInfo,productInfo).isEmpty()){
            // wish 테이블에 값을 저장
            return wishRepository.save(
                    Wish.builder()
                            .productId(productInfo)
                            .userInfo(wishUserInfo)
                            .wishDate(LocalDateTime.now())
                            .build()
            ).getWishId();
        }else{
            wishRepository.delete(wishRepository.findByUserInfoAndProductId(wishUserInfo,productInfo).get());
            return -1L;
        }

    }
    
    //위시 리스트
    @Override
    public List<ResWishListDto> wishList(UserInfo userInfo, Pageable pageable, int page) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        pageable = PageRequest.of(page, 10, Sort.by("wishId").descending());
        Page<Wish> wishPage = wishRepository.findByUserInfo(wishUserInfo,pageable);

        List<Wish> wishList = wishPage.getContent();

        List<ResWishListDto> wishListDto = new ArrayList<>();

        wishList.forEach(entity->{
            ResWishListDto listDto = new ResWishListDto();
            listDto.setWishId(entity.getWishId());
            listDto.setCompanyName(entity.getProductId().getProductCompanyId().getCompanyName());
            listDto.setProductId(entity.getProductId().getProductId());
            listDto.setProductBrand(entity.getProductId().getProductBrand());
            listDto.setProductName(entity.getProductId().getProductName());
            listDto.setProductPrice(entity.getProductId().getProductPrice());
            listDto.setWishDate(entity.getWishDate());
            listDto.setTotalElement(wishPage.getTotalElements());
            listDto.setTotalPage(wishPage.getTotalPages());
            wishListDto.add(listDto);
        });

        return wishListDto;
    }

    //선택한 제품 찜목록에서 삭제
    @Override
    public Long wishDelete(UserInfo userInfo, List<WishIdDto> wishIdDto) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );

        for(WishIdDto idDto : wishIdDto){
            Wish wish = wishRepository.findByWishId(idDto.getWishId()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("선택한 제품이 위시리스트에 존재 하지 않습니다.")
            );
            wishRepository.delete(wish);
        }


        return 1L;

    }

}
