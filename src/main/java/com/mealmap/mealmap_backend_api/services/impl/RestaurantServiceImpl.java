package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
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
}
