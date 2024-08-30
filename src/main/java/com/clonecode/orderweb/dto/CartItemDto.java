package com.clonecode.orderweb.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long itemId;
    private int cartQuantity;
}
