package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByItemId(Long itemId);
}
