package com.clonecode.orderweb.repository;

import com.clonecode.orderweb.domain.*;
import com.clonecode.orderweb.dto.ItemDetailDto;
import com.clonecode.orderweb.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ItemDetailDto> findItemDetailById(Long itemId, Pageable pageable) {
        QItem item = QItem.item;
        QReview review = QReview.review;
        QSeller seller = QSeller.seller;

        Item itemEntity = queryFactory.selectFrom(item)
                .where(item.id.eq(itemId))
                .fetchOne();

        if (itemEntity == null){
            return Optional.empty();
        }

        Seller sellerEntity = queryFactory.selectFrom(seller)
                .where(seller.id.eq(itemEntity.getSeller().getId()))
                .fetchOne();

        List<ReviewDto> reviews = queryFactory.select(Projections.constructor(ReviewDto.class,
                        review.id, review.customer.name, review.rating, review.reviewText))
                .from(review)
                .where(review.item.eq(itemEntity))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Double avgRating = queryFactory.select(review.rating.avg())
                .from(review)
                .where(review.item.eq(itemEntity))
                .fetchOne();

        Long reviewCount = queryFactory.select(review.count())
                .from(review)
                .where(review.item.eq(itemEntity))
                .fetchOne();

        ItemDetailDto itemDetailDto = new ItemDetailDto();
        itemDetailDto.setId(itemEntity.getId());
        itemDetailDto.setName(itemEntity.getName());
        itemDetailDto.setThumbnailImage(itemEntity.getThumbnailImage());
        itemDetailDto.setAverageRating(avgRating != null ? avgRating : 0.0);
        itemDetailDto.setReviewCount(reviewCount != null ? reviewCount : 0L);
        itemDetailDto.setPrice(itemEntity.getPrice());
        itemDetailDto.setDescription(itemEntity.getDescription());
        itemDetailDto.setAdditionalImages(itemEntity.getDetailImages());

        if (sellerEntity != null) {
            itemDetailDto.setSellerName(sellerEntity.getName());
            itemDetailDto.setSellerPhoneNumber(sellerEntity.getPhoneNumber());
        }

        itemDetailDto.setReviews(reviews);

        return Optional.of(itemDetailDto);
    }
}
