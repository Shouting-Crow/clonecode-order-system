package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.DeliveryStatus;
import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.Order;
import com.clonecode.orderweb.dto.CustomerOrderDto;
import com.clonecode.orderweb.dto.DeliveryDto;
import com.clonecode.orderweb.dto.OrderItemDto;
import com.clonecode.orderweb.service.ItemService;
import com.clonecode.orderweb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String showOrderForm(@RequestParam(name = "itemId") Long itemId,
                                @RequestParam(name = "quantity") Long quantity,
                                HttpSession session,
                                Model model) {

        Customer customer = (Customer) session.getAttribute("loginMember");
        if (customer == null) {
            return "redirect:/login";
        }

        Item item = itemService.getItem(itemId);
        if (item == null || item.getPrice() == null) {
            return "redirect:/error";
        }

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setCustomerId(customer.getId());
        orderItemDto.setItemId(item.getId());
        orderItemDto.setItemName(item.getName());
        orderItemDto.setThumbnailImage(item.getThumbnailImage());
        orderItemDto.setPrice(item.getPrice());
        orderItemDto.setQuantity(quantity);
        orderItemDto.setTotalPrice(item.getPrice() * quantity);

        model.addAttribute("orderItemDto", orderItemDto);

        log.info("billingAddress : {}", customer.getBillingAddress());

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryStatus(DeliveryStatus.INIT);
        deliveryDto.setDeliveryAddress(customer.getBillingAddress().toString());

        model.addAttribute("deliveryDto", deliveryDto);

        return "order/order-form";
    }

    @PostMapping("/order")
    public String makeOrder(@ModelAttribute OrderItemDto orderItemDto,
                            @ModelAttribute DeliveryDto deliveryDto,
                            Model model){

        Order order = orderService.createOrder(orderItemDto, deliveryDto);

        model.addAttribute("orderId", order.getId());
        model.addAttribute("customerName", order.getCustomer().getName());
        model.addAttribute("customerPhoneNumber", order.getCustomer().getPhoneNumber());
        model.addAttribute("deliveryAddress", order.getDelivery().getDeliveryAddress().toString());
        model.addAttribute("itemName", order.getOrderItems().get(0).getItem().getName());
        model.addAttribute("quantity", order.getOrderItems().get(0).getOrderCount());
        model.addAttribute("thumbnailImage", order.getOrderItems().get(0).getItem().getThumbnailImage());
        model.addAttribute("totalPrice", order.getOrderItems().get(0).getOrderPrice());

        return "order/order-success";
    }

    @GetMapping("/orders")
    public String getCustomerOrders(HttpSession session, Model model){
        Customer customer = (Customer) session.getAttribute("loginMember");
        if (customer == null) {
            return "redirect:/login";
        }

        List<CustomerOrderDto> orders = orderService.getCustomerOrders(customer.getId());
        model.addAttribute("orders", orders);

        return "order/order-list";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam(name = "orderId") Long orderId,
                              HttpSession session){
        Customer customer = (Customer) session.getAttribute("loginMember");
        if (customer == null){
            return "redirect:/login";
        }

        orderService.cancelOrder(orderId, customer.getId());

        return "redirect:/orders";
    }

    @PostMapping("/order/delete")
    public String deleteOrder(@RequestParam(name = "orderId") Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }



}
