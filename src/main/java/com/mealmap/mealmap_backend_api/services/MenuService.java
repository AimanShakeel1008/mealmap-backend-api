package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuItemDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;

import java.util.List;

public interface MenuService {
    MenuDto createMenuForARestaurant(Long restaurantId, MenuRequestDto menuRequestDto);

    List<MenuDto> getMenuForARestaurant(Long restaurantId);

    MenuDto addMenuItemInAMenu(Long restaurantId, Long menuId, MenuItemDto menuItem);

    MenuItemDto getMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId);

    MenuItemDto updateAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId, MenuItemDto menuItem);

    MenuItemDto updateAvailabilityOfAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId);

    MenuItemDto updateActiveStateOfAMenuItemInAMenu(Long restaurantId, Long menuId, Long menuItemId);

    MenuDto getMenuForARestaurantById(Long restaurantId, Long menuId);
}
