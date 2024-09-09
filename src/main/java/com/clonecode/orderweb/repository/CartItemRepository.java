package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
