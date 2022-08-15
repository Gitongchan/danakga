package com.danakga.webservice.review.repository;

import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* 주문번호로 후기 조회 */
    Optional<Review> findByOrders(Orders orders);

    /* 상품의 후기 목록 조회 */
    Page<Review> findByProductAndReDeleted(Product product, String deleted, Pageable pageable);
    
    /* 마이페이지 후기 목록 조회 */
    Page<Review> findByReWriter(String writer, Pageable pageable);

}
