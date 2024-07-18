package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByLoginId(String loginId);
}
