package com.danakga.webservice.Cart.service.Impl;

import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.Cart.repository.CartRepository;
import com.danakga.webservice.Cart.service.CartService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.wishlist.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public Long cartPut(UserInfo userInfo, Long cart_id, Long pd_id, String fieId, Integer cart_amount) {
        // 값 검증
        UserInfo cartUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product productInfo = productRepository.findByProductId(pd_id).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("상품 아이디를 찾을 수 없습니다")
        );

        return cartRepository.save(
                Cart.builder()
                        .pd_id(productInfo)
                        .u_id(cartUserInfo)
                        .fieId(fieId) // 몰라서 일단 작동하게 때려박아봄 ... 물어볼것 ....
                        .cart_amount(cart_amount)
                        .build()
        ).getCart_id();
    }

    @Override
    public Integer cartList() {
        if (cartRepository.selectAllCart())
        return 1L;
    }
}
