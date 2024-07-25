package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.ItemType;
import lombok.Data;

@Data
public class ItemSearchDto {

    private String keyword;
    private ItemType itemType;

}
