package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;
import com.mealmap.mealmap_backend_api.entities.Menu;
import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.respositories.MenuRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.MenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public MenuDto createMenuForARestaurant(Long restaurantId, MenuRequestDto menuRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: "+restaurantId));

        Menu menu = new Menu();
        menu.setTitle(menuRequestDto.getTitle());
        menu.setRestaurant(restaurant);

        List<MenuItem> menuItems = menuRequestDto.getItems().stream().map(itemDTO -> {
            MenuItem item = new MenuItem();
            item.setName(itemDTO.getName());
            item.setDescription(itemDTO.getDescription());
            item.setPrice(itemDTO.getPrice());
            item.setAvailable(itemDTO.getAvailable());
            item.setMenu(menu);
            return item;
        }).toList();

        menu.setItems(menuItems);
        Menu savedMenu = menuRepository.save(menu);

        return modelMapper.map(savedMenu, MenuDto.class);
    }

    @Override
    public MenuDto getMenuForARestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: "+restaurantId));

        return modelMapper.map(menuRepository.findByRestaurant(restaurant), MenuDto.class);
    }
}
