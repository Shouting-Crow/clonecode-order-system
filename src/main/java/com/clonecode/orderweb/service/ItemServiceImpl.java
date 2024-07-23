package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.dto.ItemUpdateDto;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public void registerItem(ItemRegisterDto itemRegisterDto) {
        Item item = new Item();
        makeItem(itemRegisterDto, item);
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public void updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(itemUpdateDto.getId()).orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        makeItem(itemUpdateDto, item);
        itemRepository.save(item);
    }

    @Override
    public List<Item> findSellerItem(Long sellerId) {
        return itemRepository.findBySellerId(sellerId);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemUpdateDto getItemDto(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        ItemUpdateDto dto = new ItemUpdateDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSerialNumber(item.getSerialNumber());
        dto.setPrice(item.getPrice());
        dto.setStockQuantity(item.getStockQuantity());
        dto.setSellerId(item.getSeller().getId());
        dto.setItemType(item.getItemType());
        dto.setDescription(item.getDescription());
        dto.setThumbnailImageUrl(item.getThumbnailImage());
        dto.setDetailImageUrls(item.getDetailImages());
        return dto;
    }

    private void makeItem(ItemRegisterDto itemRegisterDto, Item item) {
        item.setName(itemRegisterDto.getName());
        item.setSerialNumber(itemRegisterDto.getSerialNumber());
        item.setPrice(itemRegisterDto.getPrice());
        item.setStockQuantity(itemRegisterDto.getStockQuantity());
        item.setSeller(sellerRepository.findById(itemRegisterDto.getSellerId()).orElseThrow());
        item.setItemType(itemRegisterDto.getItemType());
        item.setDescription(itemRegisterDto.getDescription());
        item.setThumbnailImage(itemRegisterDto.getThumbnailImageUrl());
        item.setDetailImages(itemRegisterDto.getDetailImageUrls());
    }

}
