package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.*;
import com.clonecode.orderweb.dto.DeliveryDto;
import com.clonecode.orderweb.dto.OrderItemDto;
import com.clonecode.orderweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        order.setOrderItems(Collections.singletonList(orderItem));

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
}
