package com.mealmap.mealmap_backend_api.dto;

import com.mealmap.mealmap_backend_api.entities.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;
    private MenuItemDto menuItem;
    //private CartDto cart;
    private int quantity;
}
