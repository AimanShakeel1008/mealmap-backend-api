package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.MenuItemDto;
import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.Menu;
import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.exceptions.RuntimeConflictException;
import com.mealmap.mealmap_backend_api.respositories.RestaurantRepository;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        mappedRestaurant.setActive(true);
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
    public List<RestaurantDto> getAllRestaurantsOfAnOwner(RestaurantOwner restaurantOwner) {
        return  restaurantRepository.findByOwner(restaurantOwner)
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

    @Override
    public RestaurantDto updateRestaurantDetails(Long restaurantId, RestaurantDto restaurantDto) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the details of the restaurant as you are not the owner of this restaurant");
        }

        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setHoursOfOperation(restaurantDto.getHoursOfOperation());
        restaurant.setCuisineType(restaurantDto.getCuisineType());
        restaurant.setContactNumber(restaurantDto.getContactNumber());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto updateAvailabilityOfAMenuItemInAMenu(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        restaurant.setOpen(!restaurant.getOpen());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto updateActiveStateOfAMenuItemInAMenu(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();

        if (!Objects.equals(restaurant.getOwner().getId(), currentRestaurantOwner.getId())) {
            throw new RuntimeException("You can not update the menu of the restaurant as you are not the owner of this restaurant");
        }

        restaurant.setActive(!restaurant.getActive());
        restaurant.setOpen(!restaurant.getActive());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }
}
