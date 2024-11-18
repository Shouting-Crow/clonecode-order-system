package com.clonecode.orderweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long itemId;
    private Long customerId;
    private String itemName;
    private String thumbnailImage;
    private Long price;
    private Long quantity;
    private Long totalPrice;
    private String paymentMethod;
    private String additionalPaymentMethod;

    public OrderItemDto(Long itemId, String itemName, Long price, Long totalPrice, Long quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }
}
