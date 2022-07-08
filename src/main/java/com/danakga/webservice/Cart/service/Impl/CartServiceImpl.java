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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public Long cartPut(UserInfo userInfo, Long cartId, Long productId, Integer cartAmount) {
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product productInfo = productRepository.findByProductId(productId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다")
        );

        return cartRepository.save(
                Cart.builder()
                        .productId(productInfo)
                        .userInfo(cartUserInfo)
                        .cartAmount(1)
                        .build()
        ).getCartId();
    }

    @Override
    public List<Cart> cartList() {
            return cartRepository.findByIdcAndCartIdAndProductIdAndUserInfoAndCartAmount();
    }

    @Override
    public void addItem(Cart cart, Long cartId) {
        if(cartRepository.findByCartId(cartId).isPresent()){
            int count = cart.getCartAmount();
             count = count + 1;
             cart.setCartAmount(count);
        }
    }

    @Override
    public void subItem(Cart cart, Long cartId) {
        if(cartRepository.findByCartId(cartId).isPresent()){
            int count = cart.getCartAmount();
            count = count - 1;
            cart.setCartAmount(count);
        }
    }

}
