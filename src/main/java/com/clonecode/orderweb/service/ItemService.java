package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.ItemListDto;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.dto.ItemSearchDto;
import com.clonecode.orderweb.dto.ItemUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    void registerItem(ItemRegisterDto itemRegisterDto);
    List<Item> findSellerItem(Long sellerId);
    void updateItem(ItemUpdateDto itemUpdateDto);
    void deleteItem(Long id);
    ItemUpdateDto getItemDto(Long id);
    Page<ItemListDto> getItemList(Pageable pageable);
    Page<ItemListDto> searchItems(ItemSearchDto itemSearchDto, Pageable pageable);
}
