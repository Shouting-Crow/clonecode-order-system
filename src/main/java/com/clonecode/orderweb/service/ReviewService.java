package com.clonecode.orderweb.service;

import com.clonecode.orderweb.dto.ReviewRegisterDto;

public interface ReviewService {
    void registerReview(Long itemId, Long customerId, ReviewRegisterDto reviewRegisterDto);
}
