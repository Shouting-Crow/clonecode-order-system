package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.Cart;
import com.clonecode.orderweb.domain.CartItem;
import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.dto.CartItemDto;
import com.clonecode.orderweb.dto.CartOrderDto;
import com.clonecode.orderweb.repository.CartItemRepository;
import com.clonecode.orderweb.repository.CartRepository;
import com.clonecode.orderweb.repository.CustomerRepository;
import com.clonecode.orderweb.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> getCartItems(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        return cart.getCartItems();
    }

    @Override
    @Transactional
    public void addItemToCart(Long customerId, CartItemDto cartItemDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객을 찾을 수 없습니다."));

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(ci -> ci.getItem().equals(item))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setCartQuantity(existingCartItem.getCartQuantity() + cartItemDto.getCartQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setItem(item);
            cartItem.setCartQuantity(cartItemDto.getCartQuantity());
            cartItem.setPrice(BigDecimal.valueOf(item.getPrice()));
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
    }

    @Override
    public List<CartOrderDto> getCartItemsForOrder(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        List<CartOrderDto> cartOrderDtos = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            CartOrderDto cartOrderDto = new CartOrderDto();
            cartOrderDto.setCartItemId(cartItem.getId());
            cartOrderDto.setItemId(cartItem.getItem().getId());
            cartOrderDto.setQuantity(cartItem.getCartQuantity());
            cartOrderDto.setPrice(cartItem.getPrice().longValue());

            cartOrderDtos.add(cartOrderDto);
        }

        return cartOrderDtos;
    }

    @Override
    @Transactional
    public void clearCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
