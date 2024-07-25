package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.ItemListDto;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    void registerItem(ItemRegisterDto itemRegisterDto);
    List<Item> findSellerItem(Long sellerId);
    void updateItem(ItemUpdateDto itemUpdateDto);
    void deleteItem(Long id);
    ItemUpdateDto getItemDto(Long id);
    List<ItemListDto> getItemList();
}
