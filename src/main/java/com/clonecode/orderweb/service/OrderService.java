package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Order;
import com.clonecode.orderweb.dto.DeliveryDto;
import com.clonecode.orderweb.dto.OrderItemDto;

public interface OrderService {
    Order createOrder(OrderItemDto orderItemDto, DeliveryDto deliveryDto);
}
