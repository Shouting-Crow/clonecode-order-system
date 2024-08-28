package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Order;
import com.clonecode.orderweb.dto.CustomerOrderDto;
import com.clonecode.orderweb.dto.DeliveryDto;
import com.clonecode.orderweb.dto.OrderItemDto;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderItemDto orderItemDto, DeliveryDto deliveryDto);
    List<CustomerOrderDto> getCustomerOrders(Long customerId);
    void cancelOrder(Long orderId, Long customerId);
    void deleteOrder(Long orderId);
}
