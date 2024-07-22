package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        item.setName(itemRegisterDto.getName());
        item.setSerialNumber(itemRegisterDto.getSerialNumber());
        item.setPrice(itemRegisterDto.getPrice());
        item.setStockQuantity(itemRegisterDto.getStockQuantity());
        item.setSeller(sellerRepository.findById(itemRegisterDto.getSellerId()).orElseThrow());
        item.setItemType(itemRegisterDto.getItemType());
        item.setDescription(itemRegisterDto.getDescription());
        item.setThumbnailImage(itemRegisterDto.getThumbnailImageUrl());
        item.setDetailImages(itemRegisterDto.getDetailImageUrls());

        itemRepository.save(item);
    }

    @Override
    public List<Item> findSellerItem(Long sellerId) {
        return itemRepository.findBySellerId(sellerId);
    }
}
