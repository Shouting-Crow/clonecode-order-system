package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findBySellerId(Long sellerId);
}
