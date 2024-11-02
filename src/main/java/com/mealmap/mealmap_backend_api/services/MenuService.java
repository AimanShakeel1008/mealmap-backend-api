package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;

public interface MenuService {
    MenuDto createMenuForARestaurant(Long restaurantId, MenuRequestDto menuRequestDto);

    MenuDto getMenuForARestaurant(Long restaurantId);
}
