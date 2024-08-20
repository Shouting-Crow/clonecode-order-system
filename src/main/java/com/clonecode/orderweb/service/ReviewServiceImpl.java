package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.Review;
import com.clonecode.orderweb.dto.ReviewRegisterDto;
import com.clonecode.orderweb.repository.CustomerRepository;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void registerReview(Long itemId, Long customerId, ReviewRegisterDto reviewRegisterDto) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Review review = new Review();
        review.setCustomer(customer);
        review.setItem(item);
        review.setRating(reviewRegisterDto.getRating());
        review.setReviewText(reviewRegisterDto.getReviewText());

        reviewRepository.save(review);
    }
}














