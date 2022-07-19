package com.danakga.webservice.Cart.service.Impl;

import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.Cart.repository.CartRepository;
import com.danakga.webservice.Cart.service.CartService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    @Transactional
    @Override
    public Long cartPut(UserInfo userInfo, Cart cart, Long productId) {
        // 장바구니 담기
        // 유저 id로 해당 유저의 장바구니 찾기
        cart = cartRepository.findByCartId();

        // 장바구니가 존재하지 않는다면
        if (cart == null) {
            UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
            );
            Product productInfo = productRepository.findByProductId(productId).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다")
            );

            return cartRepository.save(
                    Cart.builder()
                            .productId(productInfo)
                            .userInfo(cartUserInfo)
                            .price(cart.getPrice())
                            .cartAmount(1)
                            .build()
            ).getCartId();
            return 1L;
        }
        // 상품이 장바구니에 이미 존재한다면 수량만 증가
        else {


            return 1L;
        }

        // 카트 상품 총 개수 증가
        return -1L;
    }




}


