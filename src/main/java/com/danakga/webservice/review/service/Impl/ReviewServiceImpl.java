package com.danakga.webservice.review.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.orders.repository.OrdersRepository;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.review.model.Review;
import com.danakga.webservice.review.repository.ReviewRepository;
import com.danakga.webservice.review.service.ReviewService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;

    /* 후기 작성 */
    @Override
    public ResResultDto reviewWrite(ReqReviewDto reqReviewDto, UserInfo userInfo) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Product checkProduct = productRepository.findById(reqReviewDto.getProductId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Orders checkOrders = ordersRepository.findByOrdersIdAndProduct(reqReviewDto.getOrderId(), checkProduct)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("주문 내역을 찾을 수 없습니다."));

        //null이 아니면 exception, else이면 리뷰 작성
        if(reviewRepository.findByOrders(checkOrders).isPresent()) {
            Review checkReview = reviewRepository.findByOrders(checkOrders).get();

            return new ResResultDto(checkReview.getRId(), "주문 번호로 작성된 리뷰가 존재합니다.");

        } else {
            Review review = reviewRepository.save(
                    Review.builder()
                            .rWriter(checkUserInfo.getUserid())
                            .rContent(reqReviewDto.getReviewContent())
                            .rScore(reqReviewDto.getReviewScore())
                            .orders(checkOrders)
                            .userInfo(checkUserInfo)
                            .build()
            );

            return new ResResultDto(review.getRId(), "후기를 작성 했습니다.");
        }
    }
}
