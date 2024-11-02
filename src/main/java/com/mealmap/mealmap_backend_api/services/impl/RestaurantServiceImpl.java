package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.MenuRequestDto;
import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.*;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.MenuRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final MenuRepository menuRepository;


    @Override
    public RestaurantDto createNewRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantDto.getName()).orElse(null);

        if (restaurant != null) throw new RuntimeConflictException("Cannot register restaurant, restaurant already exists with name " +restaurantDto.getName());

        Restaurant mappedRestaurant = modelMapper.map(restaurantDto, Restaurant.class);

        mappedRestaurant.setOpen(true);
        mappedRestaurant.setOwner(RestaurantOwner.builder().id(1L).build());

        Restaurant savedRestaurant = restaurantRepository.save(mappedRestaurant);

        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto getRestaurantByName(String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with name: " + restaurantName));

        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class))
                .toList();
    }

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
