package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.CartDto;
import com.mealmap.mealmap_backend_api.entities.Cart;

public interface CartService {
    CartDto addToCart(Long menuItemId, int quantity);

    void clearCart(Cart cart);
}
