package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.respositories.RestaurantOwnerRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final RestaurantOwnerRepository restaurantOwnerRepository;
    private final RestaurantService restaurantService;

    @Override
    @Transactional
    public UserDto register(SignupDto signupDto) {
        User savedUser = authService.signup(signupDto, Set.of(Role.RESTAURANT_OWNER));
        createNewRestaurantOwner(savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public RestaurantOwner createNewRestaurantOwner(User user) {

        RestaurantOwner restaurantOwner = RestaurantOwner.builder()
                .user(user)
                .build();

        return restaurantOwnerRepository.save(restaurantOwner);
    }

    @Override
    public RestaurantDto registerRestaurant(RestaurantDto restaurantDto) {
        return restaurantService.createNewRestaurant(restaurantDto);
    }

    @Override
    public RestaurantDto getRestaurantById(Long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @Override
    public RestaurantDto getRestaurantByName(String restaurantName) {
        return restaurantService.getRestaurantByName(restaurantName);
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
