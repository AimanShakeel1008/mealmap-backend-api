package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.RestaurantOwnerRepository;
import com.mealmap.mealmap_backend_api.services.AuthService;
import com.mealmap.mealmap_backend_api.services.MenuService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserDto register(RestaurantOwnerSignupDto restaurantOwnerSignupDto) {

        SignupDto signupDto = SignupMapper.extractUserSpecificDetails(restaurantOwnerSignupDto);

        User savedUser = authService.signup(signupDto, Set.of(Role.RESTAURANT_OWNER));

        createNewRestaurantOwner(restaurantOwnerSignupDto, savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public RestaurantOwner createNewRestaurantOwner(RestaurantOwnerSignupDto restaurantOwnerSignupDto, User user) {

        RestaurantOwner restaurantOwner = RestaurantOwner.builder()
                .user(user)
                .contactNumber(restaurantOwnerSignupDto.getContactNumber())
                .build();

        return restaurantOwnerRepository.save(restaurantOwner);
    }

    @Override
    public RestaurantOwner getCurrentRestaurantOwner() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return restaurantOwnerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Customer not associated with user with id: "+user.getId()
        ));
    }
}
