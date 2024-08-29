package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByOrderItemsItemSellerId(Long sellerId);
}
