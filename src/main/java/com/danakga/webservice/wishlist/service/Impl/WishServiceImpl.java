package com.danakga.webservice.wishlist.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.wishlist.model.Wish;
import com.danakga.webservice.wishlist.repository.WishRepository;
import com.danakga.webservice.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // 오토와이드가 필요 없음
public class WishServiceImpl implements WishService {
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

// 유저가 프러덕트 아이가 있으면 삭제 -> 레퍼지토리 생성
    @Override
    public Long wishList(UserInfo userInfo, Long product_id) {
        //검증을 해줘야함
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow( // 객체의 아이디 값을 조회
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product productInfo = productRepository.findByProductId(product_id).orElseThrow( // 파라미터로 long 타입을 갖기 때문
                ()->new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다")
        );

        // wish 테이블에 값을 저장
        return wishRepository.save(
                Wish.builder()
                        .pd_id(productInfo)
                        .u_id(wishUserInfo)
                        .wish_date(LocalDateTime.now())
                        .build()
        ).getWish_id();

    }

    @Override
    public Long wishCheck(UserInfo userInfo, Long product_id) {
        if (wishRepository.findByUserInfoAndProduct_id(userInfo,product_id).isPresent()) {
            return wishList(userInfo, product_id); //같은 userid있으면 -1반환
        }
        return wishDelete(userInfo,product_id);
    }


    @Override
    public Long wishDelete(UserInfo userInfo, Long product_id) {

        Wish wish = wishRepository.findByUserInfoAndProduct_id(userInfo,product_id).orElseThrow(
                ()-> new CustomException.ResourceNotFoundException("찜한 상품이 없습니다")
        );

        wishRepository.delete(wish);

        return null;
    }

}
