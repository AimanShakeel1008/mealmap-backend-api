package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.respositories.RestaurantOwnerRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final RestaurantOwnerRepository restaurantOwnerRepository;

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
}
