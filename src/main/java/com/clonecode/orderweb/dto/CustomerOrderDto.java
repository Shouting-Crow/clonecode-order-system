package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerOrderDto {
    private Long orderId;
    private Long itemId;
    private String itemName;
    private String thumbnailImage;
    private Long price;
    private Long quantity;
    private Long totalPrice;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private String orderStatus;

    private List<OrderItemDto> orderItems;
}
