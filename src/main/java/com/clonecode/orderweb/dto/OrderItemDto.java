package com.clonecode.orderweb.dto;

import lombok.Data;

@Data
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
}
