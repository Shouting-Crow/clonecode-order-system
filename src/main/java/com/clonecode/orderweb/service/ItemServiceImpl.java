package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.QItem;
import com.clonecode.orderweb.domain.QReview;
import com.clonecode.orderweb.domain.Review;
import com.clonecode.orderweb.dto.*;
import com.clonecode.orderweb.repository.ItemRepository;
import com.clonecode.orderweb.repository.ReviewRepository;
import com.clonecode.orderweb.repository.SellerRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public Page<ItemListDto> getItemList(Pageable pageable) {
        Page<Item> pagedItems = itemRepository.findAll(pageable);
        return pagedItems.map(this::convertToDto);
    }

    @Override
    public Page<ItemListDto> searchItems(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QReview review = QReview.review;
        BooleanBuilder builder = new BooleanBuilder();

        if (itemSearchDto.getKeyword() != null && !itemSearchDto.getKeyword().isEmpty()){
            builder.and(item.name.containsIgnoreCase(itemSearchDto.getKeyword()));
        }

        if (itemSearchDto.getItemType() != null){
            builder.and(item.itemType.eq(itemSearchDto.getItemType()));
        }

        JPQLQuery<Item> query = queryFactory
                .selectFrom(item)
                .leftJoin(item.reviews, review)
                .where(builder)
                .groupBy(item.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if ("priceAsc".equals(itemSearchDto.getSortType())){
            query.orderBy(item.price.asc());
        } else if ("priceDesc".equals(itemSearchDto.getSortType())){
            query.orderBy(item.price.desc());
        } else if ("ratingAsc".equals(itemSearchDto.getSortType())){
            query.orderBy(review.rating.avg().coalesce(0.0).asc());
        } else if ("ratingDesc".equals(itemSearchDto.getSortType())){
            query.orderBy(review.rating.avg().coalesce(0.0).desc());
        } else if ("reviewCountAsc".equals(itemSearchDto.getSortType())){
            query.orderBy(item.reviews.size().asc());
        } else if ("reviewCountDesc".equals(itemSearchDto.getSortType())){
            query.orderBy(item.reviews.size().desc());
        }

        List<Item> items = query.fetch();

        List<ItemListDto> itemDtos = items.stream()
                .map(this::convertToDto)
                .toList();

        long totalCount = query.fetchCount();

        return new PageImpl<>(itemDtos, pageable, totalCount);
    }

    @Override
    public Optional<ItemDetailDto> getItemDetail(Long itemId, Pageable pageable) {
        return itemRepository.findItemDetailById(itemId, pageable);
    }

    private ItemListDto convertToDto(Item item) {
        ItemListDto dto = new ItemListDto();
        dto.setId(item.getId());
        dto.setThumbnailImage(item.getThumbnailImage());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setItemType(item.getItemType());

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
