package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.QItem;
import com.clonecode.orderweb.domain.Review;
import com.clonecode.orderweb.dto.ItemListDto;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.dto.ItemSearchDto;
import com.clonecode.orderweb.dto.ItemUpdateDto;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.ReviewRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;
    private final ReviewRepository reviewRepository;
    private final JPAQueryFactory queryFactory;

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

    @Override
    public List<ItemListDto> getItemList() {
        List<Item> items = itemRepository.findAll();
        return itemToListDto(items);
    }

    @Override
    public List<ItemListDto> searchItems(ItemSearchDto itemSearchDto) {
        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();

        if (itemSearchDto.getKeyword() != null && !itemSearchDto.getKeyword().isEmpty()){
            builder.and(item.name.containsIgnoreCase(itemSearchDto.getKeyword()));
        }

        if (itemSearchDto.getItemType() != null){
            builder.and(item.itemType.eq(itemSearchDto.getItemType()));
        }

        List<Item> items = queryFactory.selectFrom(item)
                .where(builder)
                .fetch();

        return itemToListDto(items);
    }

    private List<ItemListDto> itemToListDto(List<Item> items) {
        return items.stream().map(item -> {
            ItemListDto dto = new ItemListDto();
            dto.setId(item.getId());
            dto.setThumbnailImage(item.getThumbnailImage());
            dto.setName(item.getName());
            dto.setPrice(item.getPrice());

            List<Review> reviews = reviewRepository.findByItemId(item.getId());
            if (!reviews.isEmpty()) {
                double averageRating = reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                dto.setAverageRating(averageRating);
                dto.setReviewCount((long) reviews.size());
            } else {
                dto.setAverageRating(0.0);
                dto.setReviewCount(0L);
            }
            return dto;
        }).collect(Collectors.toList());
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
