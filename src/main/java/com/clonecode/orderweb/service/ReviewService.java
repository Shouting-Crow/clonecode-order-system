package com.clonecode.orderweb.service;

import com.clonecode.orderweb.dto.ReviewDto;
import com.clonecode.orderweb.dto.ReviewRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    void registerReview(Long itemId, Long customerId, ReviewRegisterDto reviewRegisterDto);
    void deleteReview(Long reviewId, Long customerId);
    Page<ReviewDto> getPagedReviews(Long itemId, Pageable pageable);
}
