package com.clonecode.orderweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDetailDto {

    private Long id;
    private String name;
    private String thumbnailImage;
    private Double averageRating;
    private Long reviewCount;
    private Long price;
    private String description;
    private List<String> additionalImages;

    private String sellerName;
    private String sellerPhoneNumber;

    private List<ReviewDto> reviews;
}
