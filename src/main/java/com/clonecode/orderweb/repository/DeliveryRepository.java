package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
