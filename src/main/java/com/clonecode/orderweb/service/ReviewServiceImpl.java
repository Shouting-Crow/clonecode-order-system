package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.Review;
import com.clonecode.orderweb.dto.ReviewDto;
import com.clonecode.orderweb.dto.ReviewRegisterDto;
import com.clonecode.orderweb.repository.CustomerRepository;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long customerId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()){
            Review review = reviewOptional.get();

            if (review.getCustomer().getId().equals(customerId)) {
                reviewRepository.deleteById(reviewId);
            } else {
                throw new IllegalStateException("리뷰를 삭제할 권한이 없습니다.");
            }
        } else {
            throw new IllegalStateException("리뷰를 찾을 수 없습니다.");
        }
    }

    @Override
    public Page<ReviewDto> getPagedReviews(Long itemId, Pageable pageable) {

        Page<Review> reviewPage = reviewRepository.findByItemId(itemId, pageable);

        Page<ReviewDto> reviewDtoPage = reviewPage.map(review ->
                new ReviewDto(review.getId(), review.getCustomer().getName(), review.getRating(), review.getReviewText()));

        return reviewDtoPage;
    }
}














