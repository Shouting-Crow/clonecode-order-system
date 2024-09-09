package com.clonecode.orderweb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartOrderDto {
    private Long cartItemId;
    private Long itemId;
    private int quantity;
    private Long price;
}
