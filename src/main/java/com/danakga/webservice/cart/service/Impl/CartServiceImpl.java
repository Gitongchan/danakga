package com.danakga.webservice.cart.service.Impl;

import com.danakga.webservice.cart.dto.request.CartDto;
import com.danakga.webservice.cart.dto.request.CartIdDto;
import com.danakga.webservice.cart.dto.response.ResCartDto;
import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.cart.repository.CartRepository;
import com.danakga.webservice.cart.service.CartService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    //장바구니 추가
    @Override
    @Transactional
    public Long cartSave(UserInfo userInfo, List<CartDto> cartDtoList) {
        // 장바구니 담기
        // 유저 id로 해당 유저의 장바구니 찾기
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );

        for (CartDto cartDto : cartDtoList) {
            Product productInfo = productRepository.findByProductIdAndCompanyEnabled(cartDto.getProductId()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다 : " + cartDto.getProductId())
            );
   
            // 장바구니에 존재하지 않는다면
            if (cartRepository.findByUserInfoAndProductId(cartUserInfo, productInfo).isEmpty()) {

                cartRepository.save(
                        Cart.builder()
                                .productId(productInfo)
                                .userInfo(cartUserInfo)
                                .cartAmount(cartDto.getCartAmount())
                                .build()
                );
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

            }
        }
        return 1L;
    }

    @Override
    @Transactional
    public void cartDeleteAll(UserInfo userInfo) {
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );

        cartRepository.deleteAllByUserInfo(cartUserInfo);
    }

    @Override
    @Transactional
    public void MyCartDelete(UserInfo userInfo, List<CartIdDto> cartIdDtoList) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        for (CartIdDto deleteDto : cartIdDtoList) {
            Cart deleteCart = cartRepository.findByCartIdAndUserInfo(deleteDto.getCartId(),detailUserInfo).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("저장된 제품 정보를 찾을 수 없습니다.")
            );

            cartRepository.delete(deleteCart);
        }
    }

    @Override
    public List<ResCartDto> cartList(UserInfo userInfo) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );
        List<Cart> cartList = cartRepository.findByUserInfo(detailUserInfo);

        List<ResCartDto> resCartDtoList = new ArrayList<>();

        cartList.forEach(entity->{
                ResCartDto listDto = new ResCartDto();
                listDto.setCartId(entity.getCartId());
                listDto.setCartAmount(entity.getCartAmount());
                listDto.setCompanyId(entity.getProductId().getProductCompanyId().getCompanyId());
                listDto.setCompanyName(entity.getProductId().getProductCompanyId().getCompanyName());
                listDto.setProductId(entity.getProductId().getProductId());
                listDto.setProductName(entity.getProductId().getProductName());
                listDto.setProductPhoto(entity.getProductId().getProductPhoto());
                listDto.setProductType(entity.getProductId().getProductType());
                listDto.setProductSubType(entity.getProductId().getProductSubType());
                listDto.setProductBrand(entity.getProductId().getProductBrand());
                listDto.setProductPrice(entity.getProductId().getProductPrice());
                resCartDtoList.add(listDto);
            }
        );

        return resCartDtoList;
    }



}


