package com.clonecode.orderweb.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SellerOrderDto {
    private Long orderId;
    private Long itemId;
    private String itemName;
    private Long quantity;
    private Long totalPrice;
    private String orderStatus;
    private String customerName;
    private String customerPhoneNumber;
    private String deliveryAddress;
    private LocalDateTime orderDate;

    private List<OrderItemDto> orderItems;
}
