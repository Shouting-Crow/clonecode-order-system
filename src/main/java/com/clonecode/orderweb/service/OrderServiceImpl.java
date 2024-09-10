package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.*;
import com.clonecode.orderweb.dto.*;
import com.clonecode.orderweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    private final CartService cartService;

    @Override
    @Transactional
    public Order createOrder(OrderItemDto orderItemDto, DeliveryDto deliveryDto) {

        Customer customer = customerRepository.findById(orderItemDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 구매자 회원입니다."));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.FINISHED);

        Item item = itemRepository.findById(orderItemDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다."));

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setOrderCount(orderItemDto.getQuantity());
        orderItem.setOrderPrice(orderItemDto.getPrice() * orderItemDto.getQuantity());

        order.getOrderItems().add(orderItem);

        String stringAddress = deliveryDto.getDeliveryAddress();
        String[] addressParts = stringAddress.split(" ", 2);
        if (addressParts.length < 2) {
            throw new IllegalArgumentException("주소 형식이 올바르지 않습니다.");
        }
        String city = addressParts[0];
        String streetAddress = addressParts[1];

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryAddress(new Address(city, streetAddress));
        delivery.setStatus(deliveryDto.getDeliveryStatus());

        order.setDelivery(delivery);

        deliveryRepository.save(delivery);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPayMethod(orderItemDto.getPaymentMethod());
        payment.setPayStatus(PayStatus.INIT);
        paymentRepository.save(payment);

        return orderRepository.save(order);
    }

    @Override
    public List<CustomerOrderDto> getCustomerOrders(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream().map(order -> {
            CustomerOrderDto customerOrderDto = new CustomerOrderDto();
            customerOrderDto.setOrderId(order.getId());
            customerOrderDto.setOrderDate(order.getOrderDate());
            customerOrderDto.setOrderStatus(order.getOrderStatus().toString());

            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(orderItem -> {
                        OrderItemDto orderItemDto = new OrderItemDto();
                        orderItemDto.setItemId(orderItem.getItem().getId());
                        orderItemDto.setItemName(orderItem.getItem().getName());
                        orderItemDto.setQuantity(orderItem.getOrderCount());
                        orderItemDto.setPrice(orderItem.getOrderPrice());
                        return orderItemDto;
                    }).toList();

            customerOrderDto.setOrderItems(orderItemDtos);

            return customerOrderDto;
        }).toList();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getCustomer().getId().equals(customerId)){
            throw new IllegalArgumentException("주문을 취소할 권한이 없습니다.");
        }

        if (order.getOrderStatus() == OrderStatus.DELIVERING){
            throw new IllegalStateException("배송 중인 주문을 취소할 수는 없습니다.");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<SellerOrderDto> getOrdersBySellerId(Long sellerId) {
        List<Order> orders = orderRepository.findByOrderItemsItemSellerId(sellerId);
        return orders.stream().map(this::convertToSellerOrderDto).toList();
    }

    @Override
    @Transactional
    public void deliveryOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.setOrderStatus(OrderStatus.DELIVERING);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelSellerOrder(Long orderId, Long sellerId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getOrderItems().get(0).getItem().getSeller().getId().equals(sellerId)){
            throw new IllegalArgumentException("취소할 권한이 없습니다.");
        }

        if (order.getOrderStatus() == OrderStatus.DELIVERING){
            throw new IllegalStateException("배송 중인 주문을 취소할 수는 없습니다.");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void createOrdersFromCart(Long customerId, List<CartOrderDto> cartOrderDtos, Address address) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.FINISHED);

        Delivery delivery = new Delivery();
        delivery.setDeliveryAddress(address);
        delivery.setOrder(order);
        order.setDelivery(delivery);

        for (CartOrderDto cartOrderDto : cartOrderDtos) {
            Item item = itemRepository.findById(cartOrderDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("제품 정보를 찾을 수 없습니다."));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setOrderCount((long)cartOrderDto.getQuantity());
            orderItem.setOrderPrice(cartOrderDto.getPrice() * cartOrderDto.getQuantity());

            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        cartService.clearCart(customerId);
    }

    private SellerOrderDto convertToSellerOrderDto(Order order){
        SellerOrderDto dto = new SellerOrderDto();
        dto.setOrderId(order.getId());
        dto.setItemId(order.getOrderItems().get(0).getItem().getId());
        dto.setItemName(order.getOrderItems().get(0).getItem().getName());
        dto.setQuantity(order.getOrderItems().get(0).getOrderCount());
        dto.setTotalPrice(order.getOrderItems().get(0).getOrderPrice() * order.getOrderItems().get(0).getOrderCount());
        dto.setOrderStatus(order.getOrderStatus().toString());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setCustomerPhoneNumber(order.getCustomer().getPhoneNumber());
        dto.setDeliveryAddress(order.getDelivery().getDeliveryAddress().toString());
        dto.setOrderDate(order.getOrderDate());

        List<OrderItemDto> orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(
                        orderItem.getItem().getId(),
                        orderItem.getItem().getName(),
                        orderItem.getOrderCount(),
                        orderItem.getOrderPrice() * orderItem.getOrderCount()
                )).toList();

        dto.setOrderItems(orderItems);

        return dto;
    }
}














