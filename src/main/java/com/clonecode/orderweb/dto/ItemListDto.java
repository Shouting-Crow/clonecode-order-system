package com.clonecode.orderweb.dto;

import lombok.Data;

@Data
public class ItemListDto {

    private Long id;
    private String thumbnailImage;
    private String name;
    private Double averageRating;
    private Long price;
    private Long reviewCount;

}
