package com.danakga.webservice.review.repository;

import com.danakga.webservice.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
