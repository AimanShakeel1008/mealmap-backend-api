package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.MenuRepository;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final RestaurantOwnerService restaurantOwnerService;


    @Override
    public RestaurantDto createNewRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantDto.getName()).orElse(null);

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (restaurant != null) throw new RuntimeConflictException("Cannot register restaurant, restaurant already exists with name " +restaurantDto.getName());

        Restaurant mappedRestaurant = modelMapper.map(restaurantDto, Restaurant.class);

        mappedRestaurant.setOpen(true);
        mappedRestaurant.setOwner(RestaurantOwner.builder().id(currentRestaurantOwner.getId()).build());

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
    public List<RestaurantDto> getRestaurantsByCuisine(String cuisineType) {
        return restaurantRepository.findRestaurantByCuisineType(cuisineType)
                .stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class))
                .toList();
    }
}
