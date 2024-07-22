package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.ItemRegisterDto;

import java.util.List;

public interface ItemService {
    void registerItem(ItemRegisterDto itemRegisterDto);
    List<Item> findSellerItem(Long sellerId);
}
