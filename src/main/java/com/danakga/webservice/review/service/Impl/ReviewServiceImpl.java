package com.danakga.webservice.review.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.orders.model.OrdersStatus;
import com.danakga.webservice.orders.repository.OrdersRepository;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.review.dto.request.ReqReviewDeleteDto;
import com.danakga.webservice.review.dto.request.ReqReviewDto;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.review.model.Review;
import com.danakga.webservice.review.repository.ReviewRepository;
import com.danakga.webservice.review.service.ReviewService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;

    /* 후기 목록 */
    @Override
    public ResReviewListDto reviewList(Long p_id, Pageable pageable, int page) {

        final String deleted = "N";

        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        pageable = PageRequest.of(page, 10, Sort.by("reCreated").descending());
        Page<Review> checkReview = reviewRepository.findByProductAndReDeleted(checkProduct, deleted, pageable);

        List<Map<String, Object>> reviewList = new ArrayList<>();

        Map<String, Object> reviewMap = new LinkedHashMap<>();

        checkReview.forEach(review -> {
            reviewMap.put("re_id", review.getReId());
            reviewMap.put("re_content", review.getReContent());
            reviewMap.put("re_created", review.getReCreated());
            reviewMap.put("re_score", review.getReScore());
            reviewMap.put("re_writer", review.getReWriter());
            reviewMap.put("totalPages", checkReview.getTotalPages());
            reviewMap.put("totalElements", checkReview.getTotalElements());

            reviewList.add(reviewMap);
        });

        return new ResReviewListDto(reviewList);
    }


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
        if (reviewRepository.findByOrders(checkOrders).isPresent()) {
            Review checkReview = reviewRepository.findByOrders(checkOrders).get();

            return new ResResultDto(checkReview.getReId(), "주문 번호로 작성된 리뷰가 존재합니다.");
        }

        Review review = reviewRepository.save(
                Review.builder()
                        .reWriter(checkUserInfo.getUserid())
                        .reContent(reqReviewDto.getReviewContent())
                        .reScore(reqReviewDto.getReviewScore())
                        .orders(checkOrders)
                        .product(checkProduct)
                        .userInfo(checkUserInfo)
                        .build()
        );

        return new ResResultDto(review.getReId(), "후기를 작성 했습니다.");
    }


    /* 후기 수정 */
    @Override
    public ResResultDto reviewEdit(ReqReviewDto reqReviewDto, UserInfo userInfo, Long re_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Product checkProduct = productRepository.findById(reqReviewDto.getProductId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Orders checkOrders = ordersRepository.findByOrdersIdAndProduct(reqReviewDto.getOrderId(), checkProduct)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("주문 내역을 찾을 수 없습니다."));

        Review checkReview = reviewRepository.findById(re_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("작성한 후기를 찾을 수 없습니다."));

        checkReview = reviewRepository.save(
                Review.builder()
                        .reId(checkReview.getReId())
                        .reWriter(checkUserInfo.getUserid())
                        .reContent(reqReviewDto.getReviewContent())
                        .reScore(reqReviewDto.getReviewScore())
                        .reCreated(checkReview.getReCreated())
                        .reDeleted(checkReview.getReDeleted())
                        .orders(checkOrders)
                        .product(checkProduct)
                        .userInfo(checkUserInfo)
                        .build()
        );

        return new ResResultDto(checkReview.getReId(), "후기를 수정 했습니다.");
    }


    /* 후기 삭제 상태 변경 */
    @Override
    public ResResultDto reviewDelete(ReqReviewDeleteDto reqReviewDeleteDto, UserInfo userInfo, Long re_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Product checkProduct = productRepository.findById(reqReviewDeleteDto.getProductId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Orders checkOrders = ordersRepository.findById(reqReviewDeleteDto.getOrderId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("주문 내역을 찾을 수 없습니다."));

        Review checkReview = reviewRepository.findById(re_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("후기를 찾을 수 없습니다."));

        reviewRepository.updateReDeleted(re_id);

        return new ResResultDto(checkReview.getReId(), "후기를 삭제 했습니다.");
    }

    /* 후기 작성 체크 */
    @Override
    public ResResultDto reviewCheck(Long o_id) {

        Orders checkOrders = ordersRepository.findById(o_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("주문 내역을 찾을 수 없습니다."));

        /* 주문 상태 값 가져옴 */
        String orderStatus = checkOrders.getOrdersStatus();

        /* 구매한 주문번호로 후기를 작성하지 않았거나, 주문 상태가 "구매확정"인 경우에만 후기 작성 가능 */
        if (reviewRepository.findByOrders(checkOrders).isEmpty() && orderStatus.equals(OrdersStatus.CONFIRM.getStatus())) {

            return new ResResultDto(0L, "후기를 작성할 수 있습니다.");
        }
        return new ResResultDto(-1L, "이미 후기를 작성 했거나, 구매 확정을 하지 않았습니다.");
    }
}

