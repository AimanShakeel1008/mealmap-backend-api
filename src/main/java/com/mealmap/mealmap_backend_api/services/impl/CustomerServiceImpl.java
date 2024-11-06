package com.mealmap.mealmap_backend_api.services.impl;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;
import com.mealmap.mealmap_backend_api.exceptions.ResourceNotFoundException;
import com.mealmap.mealmap_backend_api.respositories.CustomerRepository;
import com.mealmap.mealmap_backend_api.services.*;
import com.mealmap.mealmap_backend_api.utils.SignupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private  final CustomerRepository customerRepository;
    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;

    @Override
    @Transactional
    public UserDto register(CustomerSignupDto customerSignupDto) {

        SignupDto signupDto = SignupMapper.extractUserSpecificDetails(customerSignupDto);

        User savedUser = authService.signup(signupDto, Set.of(Role.CUSTOMER));

        createNewCustomer(customerSignupDto, savedUser);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public Customer createNewCustomer(CustomerSignupDto customerSignupDto, User user) {

        Customer customer = Customer.builder()
                .user(user)
                .address(customerSignupDto.getAddress())
                .contactNumber(customerSignupDto.getContactNumber())
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCurrentCustomer() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Customer not associated with user with id: "+user.getId()
        ));
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
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
    public List<RestaurantDto> getRestaurantsByDishName(String dishName) {
        return menuItemService.getRestaurantsByDishName(dishName);
    }

    @Override
    public List<RestaurantDto> getRestaurantsByCuisine(String cuisineType) {
        return restaurantService.getRestaurantsByCuisine(cuisineType);
    }
}
