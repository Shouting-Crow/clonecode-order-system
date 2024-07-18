package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByLoginId(String loginId);
    Seller findByLoginId(String loginId);
}
