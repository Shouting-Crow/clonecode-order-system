package com.clonecode.orderweb.service;

import com.clonecode.orderweb.domain.CartItem;
import com.clonecode.orderweb.dto.CartItemDto;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(Long customerId);
    void addItemToCart(Long customerId, CartItemDto cartItemDto);
}
