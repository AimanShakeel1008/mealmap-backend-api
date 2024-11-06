package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;

import java.util.List;

public interface MenuService {
    MenuDto createMenuForARestaurant(Long restaurantId, MenuRequestDto menuRequestDto);

    List<MenuDto> getMenuForARestaurant(Long restaurantId);
}
