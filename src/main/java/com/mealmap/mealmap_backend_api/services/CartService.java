package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.CartDto;
import com.mealmap.mealmap_backend_api.entities.Cart;

public interface CartService {
    CartDto addToCart(Long cartId, Long menuItemId, int quantity);

    void clearCart(Cart cart);

    CartDto getCartOfACustomer();

    CartDto updateQuantityOfACartItem(Long cartId, Long cartItemId, int quantity);
}
