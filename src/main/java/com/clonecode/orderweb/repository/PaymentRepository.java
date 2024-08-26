package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
