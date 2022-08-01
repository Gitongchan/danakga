package com.danakga.webservice.cart.service.Impl;

import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.dto.request.CartIdDto;
import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.cart.repository.CartRepository;
import com.danakga.webservice.cart.service.CartService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.dto.request.MyToolIdDto;
import com.danakga.webservice.mytool.model.MyToolDetail;
import com.danakga.webservice.mytool.model.MyToolFolder;
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
// 수량 수정
    // 선택 삭제-> 최근에 참고 해서

    @Transactional
    @Override
    public Long cartPut(UserInfo userInfo,CartDto cartDto) {
        // 장바구니 담기
        // 유저 id로 해당 유저의 장바구니 찾기
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product productInfo = productRepository.findByProductId(cartDto.getProductId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다 : " + cartDto.getProductId())
        );

        // 장바구니가 존재하지 않는다면
        if (cartRepository.findByUserInfoAndProductId(cartUserInfo, productInfo).isEmpty()) {

            return cartRepository.save(
                    Cart.builder()
                            .productId(productInfo)
                            .userInfo(cartUserInfo)
                            .cartAmount(cartDto.getCartAmount())
                            .build()
            ).getCartId();
        }
        // 상품이 장바구니에 이미 존재한다면 수량만 증가
        else {
            Cart cart = cartRepository.findByUserInfoAndProductId(cartUserInfo, productInfo).get();// 객체를 가져와 담음
            cartRepository.save(
                    Cart.builder()
                            .cartId(cart.getCartId())
                            .productId(cart.getProductId())
                            .userInfo(cart.getUserInfo())
                            .cartAmount(cart.getCartAmount() + cartDto.getCartAmount())
                            .build()
            );
            return cart.getCartId();
        }

    }

    // 유저 정보만 파라미터로 받아옴
    @Override
    public Long cartDeleteAll(UserInfo userInfo) {
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );

        userRepository.deleteById(cartUserInfo.getId());
        return null;
    }


    @Override
    @Transactional
    public void MyToolDelete(UserInfo userInfo, List<CartIdDto> cartIdDtoList) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        for (CartIdDto deleteDto : cartIdDtoList) {
            Cart deleteCart = cartRepository.findByCartId(deleteDto.getCartId()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("저장된 제품 정보를 찾을 수 없습니다.")
            );

            cartRepository.delete(deleteCart);
        }
    }






//
//    @Override
//    public Long cartDelete(Long cartId) {
//        CartDto  cartDto = new CartDto();
//        Cart cart = cartRepository.findByCartId(cartDto.getCartId());
//        Long result = -1L;
//        if(cart.isPresent()){
//            cartRepository.delete(cartId);
//            result = 1L;
//        }
//        return result;
//    }


}


