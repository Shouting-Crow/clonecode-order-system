package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Address;
import com.clonecode.orderweb.domain.CartItem;
import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.dto.CartItemDto;
import com.clonecode.orderweb.dto.CartOrderDto;
import com.clonecode.orderweb.service.CartService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/cart")
    public String viewCart(@RequestParam(name = "customerId") Long customerId,
                           Model model,
                           HttpSession session) {
        Customer customer = (Customer) session.getAttribute("loginMember");

        if (customer == null){
            return "redirect:/error/access-denied";
        }

        List<CartItem> cartItems = cartService.getCartItems(customerId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("customerId", customerId);

        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCartQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalPrice", totalPrice);

        return "cart/cart-list";
    }

    @PostMapping("/cart/add")
    public String addItemToCart(@RequestParam(name = "customerId") Long customerId,
                                @ModelAttribute(name = "cartItemDto") CartItemDto cartItemDto,
                                RedirectAttributes redirectAttributes) {
        cartService.addItemToCart(customerId, cartItemDto);

        redirectAttributes.addAttribute("customerId", customerId);
        redirectAttributes.addFlashAttribute("message", "제품이 장바구니에 추가되었습니다.");

        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkoutCart(@RequestParam(name = "customerId") Long customerId,
                               @RequestParam(name = "city") String city,
                               @RequestParam(name = "streetAddress") String streetAddress,
                               RedirectAttributes redirectAttributes){
        Address address = new Address(city, streetAddress);

        List<CartOrderDto> cartOrderDtos = cartService.getCartItemsForOrder(customerId);

        orderService.createOrdersFromCart(customerId, cartOrderDtos, address);

        redirectAttributes.addFlashAttribute("message", "주문이 완료되었습니다.");

        return "redirect:/orders";
    }

}





















