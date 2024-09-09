package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Order;
import com.clonecode.orderweb.dto.*;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderItemDto orderItemDto, DeliveryDto deliveryDto);
    List<CustomerOrderDto> getCustomerOrders(Long customerId);
    void cancelOrder(Long orderId, Long customerId);
    void deleteOrder(Long orderId);
    List<SellerOrderDto> getOrdersBySellerId(Long sellerId);
    void deliveryOrder(Long orderId);
    void cancelSellerOrder(Long orderId, Long sellerId);
    void createOrdersFromCart(Long customerId, List<CartOrderDto> cartOrderDtos);
}
