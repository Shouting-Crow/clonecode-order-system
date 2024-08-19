package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.dto.ItemDetailDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepositoryCustom {
    Optional<ItemDetailDto> findItemDetailById(Long itemId, Pageable pageable);
}
