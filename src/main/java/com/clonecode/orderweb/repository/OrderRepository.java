package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
