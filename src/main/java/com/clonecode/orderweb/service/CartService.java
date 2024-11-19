package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.CartItem;
import com.clonecode.orderweb.dto.CartItemDto;
import com.clonecode.orderweb.dto.CartOrderDto;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(Long customerId);
    void addItemToCart(Long customerId, CartItemDto cartItemDto);
    List<CartOrderDto> getCartItemsForOrder(Long customerId);
    void clearCart(Long customerId);
    void removeItemFromCart(Long cartItemId, Long customerId);
}
