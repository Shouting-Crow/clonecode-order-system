package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.ItemType;
import lombok.Data;

@Data
public class ItemListDto {

    private Long id;
    private String thumbnailImage;
    private String name;
    private Double averageRating;
    private Long price;
    private Long reviewCount;
    private ItemType itemType;

}
